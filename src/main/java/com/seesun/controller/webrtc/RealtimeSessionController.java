package com.seesun.controller.webrtc;


import com.seesun.dto.webrtc.request.BootstrapRequestDTO;
import com.seesun.dto.webrtc.request.EndSessionRequestDTO;
import com.seesun.dto.webrtc.response.BootstrapResponseDTO;
import com.seesun.dto.webrtc.response.RecordingResponseDTO;
import com.seesun.service.webrtc.RealtimeSessionService;
import com.seesun.service.webrtc.SseEmitterService;
import jakarta.servlet.http.HttpServletRequest;
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

        System.out.println("test");

        // 기존 로그인 방식 (없으면 null)
        Long memberId = getLoginMemberId();

        // URL 기반 역할 강제 (테스트 모드)
        String uri = servletReq.getRequestURI();

        if (uri.contains("/mentor/")) {
            // 멘토 강제
            memberId = 1L;
        }

        if (uri.contains("/mentee/")) {
            // 멘티 익명 허용
            if (memberId == null) memberId = -1L;
        }

        return realtimeSessionService.bootstrap(req.getLectureId(), memberId);
    }

    // 테스트 용도(멘토 / 멘티 페이지 분리시)
    @PostMapping("/join")
    public BootstrapResponseDTO join(
            @RequestBody BootstrapRequestDTO req,
            HttpServletRequest servletReq
    ) {
        Long memberId = getLoginMemberId();

        // URL 기반으로 멘티 강제
        String uri = servletReq.getRequestURI();
        if (uri.contains("/mentee/")) {
            memberId = -1L; // 익명 멘티
        }

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