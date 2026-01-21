package com.seesun.controller.webrtc;


import com.seesun.dto.webrtc.request.BootstrapRequestDTO;
import com.seesun.dto.webrtc.request.EndSessionRequestDTO;
import com.seesun.dto.webrtc.response.BootstrapResponseDTO;
import com.seesun.dto.webrtc.response.RecordingResponseDTO;
import com.seesun.service.webrtc.RealtimeSessionService;
import com.seesun.service.webrtc.SseEmitterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


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

    @PostMapping("/bootstrap")
    public BootstrapResponseDTO bootstrap(@RequestBody BootstrapRequestDTO req) {
        Long memberId = getLoginMemberId();
        return realtimeSessionService.bootstrap(req.getLectureId(), memberId);
    }

    @PostMapping("/end")
    public void endSession(@RequestBody EndSessionRequestDTO req) {
        Long memberId = getLoginMemberId();
        realtimeSessionService.endSession(req.getSessionId(), memberId);
    }

    @GetMapping("/recording")
    public RecordingResponseDTO recording(@RequestParam String sessionId) {
        return realtimeSessionService.getRecording(sessionId);
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