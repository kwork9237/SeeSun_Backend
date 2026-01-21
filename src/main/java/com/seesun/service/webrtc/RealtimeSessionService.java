package com.seesun.service.webrtc;

import com.seesun.domain.webrtc.RealtimeSession;
import com.seesun.domain.webrtc.SessionStatus;
import com.seesun.dto.webrtc.response.BootstrapResponseDTO;
import com.seesun.dto.webrtc.response.RecordingResponseDTO;
import com.seesun.dto.webrtc.response.SessionEventDTO;
import com.seesun.global.exception.ErrorCode;
import com.seesun.global.exception.GlobalException;
import com.seesun.repository.webrtc.InMemoryRealtimeSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

/**
 * 작성자: 홍진기
 * 실시간 강의 세션의 모든 핵심 비즈니스 로직을 담당하는 서비스
 *
 * 주요 책임:
 *  - 멘토는 세션 생성 가능 (자동 녹화 시작)
 *  - 멘티는 수강 여부(enrolled) 검증 후 참가 가능
 *  - 멘토만 세션 종료 가능(종료 시 모든 클라이언트에게 SSE 브로드캐스트)
 *  - 녹화 파일 조회
 *
 *  ErrorCode 전체를 Janus 기반 요구사항에 맞게 처리
 */
@Service
@RequiredArgsConstructor
public class RealtimeSessionService {
    private final InMemoryRealtimeSessionRepository sessionRepo;
    private final SseEmitterService sseEmitterService;
    private final RecordingService recordingService;

    // Janus 서버 URL(환경변수 or 설정 파일에서 관리)
    private final String JANUS_URL = "https://janus.jsflux.co.kr/janus";

    /**
     * 부트스트랩 (멘토: 세션 생성/ 멘티: 참가)
     */
    public BootstrapResponseDTO bootstrap(Long lectureId, Long memberId) {

        boolean isMentor = isMentorOfLecture(lectureId, memberId);
        boolean enrolled = isEnrolled(lectureId, memberId);

        // 멘티인데 수강 이력이 없으면 접근 불가
        if (!isMentor && !enrolled) {
            throw new GlobalException(ErrorCode.WEBRTC_MENTEE_NOT_ENROLLED);
        }

        // 해당 강의의 ACTIVE 세션을 가져온다.
        RealtimeSession active = sessionRepo.findActiveByLectureId(lectureId).orElse(null);

        // 멘토의 경우
        if (isMentor) {
            // 세션이 없으면 새로 생성
            if (active == null) {
                return createSession(lectureId, memberId);
            }

            // 세션이 있는데 멘토가 아니라 권한 없음
            ensureMentor(active, memberId);

            return buildBootstrap(active, "MENTOR", active.getMentorDisplayName());
        }

        // 멘티의 경우 - ACTIVE 세션이 없으면 참여 불가
        if (active == null) {
            throw new GlobalException(ErrorCode.WEBRTC_ROOM_NOT_FOUND);
        }

        String displayName = makeDisplayName(memberId, false);

        return buildBootstrap(active, "MENTEE", displayName);
    }

    /**
     * 새로운 실시간 세션 생성 (멘토 전용)
     */
    private BootstrapResponseDTO createSession(Long lectureId, Long mentorId) {

        String sessionId = UUID.randomUUID().toString();
        String roomId = allocateRoomId(lectureId);
        String displayName = makeDisplayName(mentorId, true);

        RealtimeSession session = RealtimeSession.builder()
                .sessionId(sessionId)
                .lectureId(lectureId)
                .roomId(roomId)
                .mentorId(mentorId)
                .mentorDisplayName(displayName)
                .status(SessionStatus.ACTIVE)
                .createAt(Instant.now())
                .recordingStatus("PROCESSING")
                .build();

        sessionRepo.save(session);

        // Janus 녹화 시작
        recordingService.start(sessionId, roomId);

        return buildBootstrap(session, "MENTOR", displayName);
    }

    private BootstrapResponseDTO buildBootstrap(
            RealtimeSession s, String role,  String displayName) {

        return BootstrapResponseDTO.builder()
                .sessionId(s.getSessionId())
                .roomId(s.getRoomId())
                .janusUrl(JANUS_URL)
                .role(role)
                .displayName(displayName)
                .mentorDisplayName(s.getMentorDisplayName())
                .build();
    }

    /**
     * 멘토만 호출할 수 있는 강의 종료 기능
     * - 녹화 종료
     * - 세션 상태 ENDED 변경
     * - SSE로 SESSION_ENDED 이벤트 전체 broadcast
     */
    public void endSession(String sessionId, Long memberId) {
        RealtimeSession session = sessionRepo.findBySessionId(sessionId)
                .orElseThrow(() -> new GlobalException(ErrorCode.WEBRTC_INVALID_SESSION));

        ensureMentor(session, memberId);

        if (session.getStatus() == SessionStatus.ENDED) {
            throw new GlobalException(ErrorCode.WEBRTC_SESSION_ENDED);
        }

        // 녹화 종료
        recordingService.stop(sessionId, session.getRoomId());

        // 세션 상태를 ENDED로 갱신
        RealtimeSession ended = RealtimeSession.builder()
                .sessionId(session.getSessionId())
                .lectureId(session.getLectureId())
                .roomId(session.getRoomId())
                .mentorId(session.getMentorId())
                .mentorDisplayName(session.getMentorDisplayName())
                .status(SessionStatus.ENDED)
                .createAt(session.getCreateAt())
                .endedAt(Instant.now())
                .recordingStatus(session.getRecordingStatus())
                .recordingUrl(session.getRecordingUrl())
                .build();

        sessionRepo.save(ended);

        // SSE: 모든 참가자에게 "SESSION_ENDED" push
        sseEmitterService.broadcast(
                sessionId,
                SessionEventDTO.builder()
                        .type("SESSION_ENDED")
                        .sessionId(sessionId)
                        .build());
    }

    /**
     * 녹화 링크 조회(준비 안 되면 PROCESSING)
     */
    public RecordingResponseDTO getRecording(String sessionId) {
        RealtimeSession session = sessionRepo.findBySessionId(sessionId)
                .orElseThrow(() -> new GlobalException(ErrorCode.WEBRTC_INVALID_SESSION));

        String url = recordingService.getRecordingUrlIfReady(sessionId);

        if (url == null) {
            return RecordingResponseDTO.builder()
                    .status("PROCESSING")
                    .url(null)
                    .build();
        }

        return RecordingResponseDTO.builder()
                .status("READY")
                .url(url)
                .build();
    }

    // 내부 유틸(권한 검증 / 이름 생성)
    private void ensureMentor(RealtimeSession session, Long memberId) {
        if (!session.getMentorId().equals(memberId)) {
            throw new  GlobalException(ErrorCode.WEBRTC_MENTOR_ONLY);
        }
    }

    private boolean isMentorOfLecture(Long lectureId, Long memberId) {
        // TODO: 강의 테이블에서 memberId 조회 후 비교
        return false;
    }

    public boolean isEnrolled(Long lectureId, Long memberId) {
        // TODO: 수강 테이블에서 조회
        return true;
    }

    private String makeDisplayName(Long memberId, boolean mentor) {
        return (mentor ? "mentor-" : "mentee-")
                + memberId + "-"
                + (System.currentTimeMillis() % 9999);
    }

    private String allocateRoomId(Long lectureId) {
        // Janus 방 번호 생성 정책
        return String.valueOf(100000 + lectureId);
    }
}
