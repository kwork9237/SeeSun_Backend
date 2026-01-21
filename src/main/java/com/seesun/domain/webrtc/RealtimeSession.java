package com.seesun.domain.webrtc;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

/**
 * 작성자: 홍진기
 * 실시간 강의 세션 도메인 모델
 * - 강의 ID/ 멘토 ID / Janus ROOM ID / 녹화 상태 등을 포함
 * - DB or InMemory 저장소로 관리됨
 */
@Getter
@Builder
public class RealtimeSession {
    private String sessionId;
    private Long lectureId;
    private String roomId;

    private Long mentorId;
    private String mentorDisplayName;

    private SessionStatus status;
    private Instant createAt;
    private Instant endedAt;

    private String recordingStatus; // PROCESSING | READY
    private String recordingUrl;
}
