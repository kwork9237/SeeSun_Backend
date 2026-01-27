package com.seesun.controller.webrtc;


import com.seesun.dto.webrtc.request.BootstrapRequestDTO;
import com.seesun.dto.webrtc.request.EndSessionRequestDTO;
import com.seesun.dto.webrtc.response.BootstrapResponseDTO;
import com.seesun.dto.webrtc.response.RecordingResponseDTO;
import com.seesun.service.webrtc.JanusRoomService;
import com.seesun.service.webrtc.RealtimeSessionService;
import com.seesun.service.webrtc.SseEmitterService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.File;
import java.util.UUID;


/**
 * 실시간 강의(WebRTC + Janus) 관련 REST API 컨트롤러.
 * - Controller는 "매핑" 역할만 담당 (팀 규칙 준수)
 * - 모든 비즈니스 로직은 Service로 위임
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seesun/session")
public class RealtimeSessionController {

    private final RealtimeSessionService realtimeSessionService;
    private final SseEmitterService sseEmitterService;

    // 🔥 부족해서 오류난 부분 — 필드 추가
    private final JanusRoomService janusRoomService;

    @Value("${seesun.janus.base-url}")
    private String janusUrl;


    // 기존 멘토 / 멘티 (서버가 역할 구분할때 사용 지우지 마세요)
//    @PostMapping("/bootstrap")
//    public BootstrapResponseDTO bootstrap(@RequestBody BootstrapRequestDTO req) {
//        System.out.println("test");
//        Long memberId = getLoginMemberId();
//        return realtimeSessionService.bootstrap(req.getLectureId(), memberId);
//    }

    // 테스트 만
    @PostMapping("/bootstrap")
    public BootstrapResponseDTO bootstrap(@RequestBody BootstrapRequestDTO req,
                                          HttpServletRequest servletReq) {

        long lectureId = req.getLectureId();

        // 방이 없으면 Janus에 방 생성
        int roomId = janusRoomService.ensureRoomExists(lectureId);

        String sessionId = UUID.randomUUID().toString();
        String displayName = "mentor-" + lectureId + "-" + (int) (Math.random() * 99999);

        return new BootstrapResponseDTO(
                sessionId,
                String.valueOf(roomId),
                janusUrl,
                "MENTOR",
                displayName,
                displayName
        );
    }

    // 테스트 용도(멘토 / 멘티 페이지 분리시)
    @PostMapping("/join")
    public BootstrapResponseDTO join(@RequestBody BootstrapRequestDTO req,
                                     HttpServletRequest servletReq) {

        long lectureId = req.getLectureId();

        int roomId = janusRoomService.ensureRoomExists(lectureId);

        String sessionId = UUID.randomUUID().toString();
        String displayName = "mentee-" + lectureId + "-" + (int) (Math.random() * 99999);

        return new BootstrapResponseDTO(
                sessionId,
                String.valueOf(roomId),
                janusUrl,
                "MENTEE",
                displayName,
                null
        );
    }




    @PostMapping("/end")
    public void endSession(@RequestBody EndSessionRequestDTO req) {
        Long memberId = getLoginMemberId();
        realtimeSessionService.endSession(req.getSessionId(), memberId);
    }

    // ✅ 프론트에서 호출하는 녹화 상태 조회 API
    @GetMapping("/recording")
    public RecordingResponseDTO recording(@RequestParam String sessionId) {
        return realtimeSessionService.getRecording(sessionId);
    }

    // ✅ 녹화 파일 다운로드(또는 브라우저 재생)
    @GetMapping("/recordings/file")
    public ResponseEntity<Resource> downloadRecording(@RequestParam String sessionId) {
        File file = realtimeSessionService.getRecordingFile(sessionId);
        if (file == null || !file.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(file);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + file.getName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }


    @GetMapping("/events")
    public SseEmitter sse(@RequestParam String sessionId) {
        Long memberId = getLoginMemberId();
        String clientKey = memberId + "-" + System.currentTimeMillis();
        return sseEmitterService.connect(sessionId, clientKey);
    }

    private Long getLoginMemberId() {
        // TODO: 로그인 사용자 ID 가져오기
        return 1L;
    }
}