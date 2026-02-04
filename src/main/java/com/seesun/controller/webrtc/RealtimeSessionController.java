package com.seesun.controller.webrtc;

import com.seesun.dto.webrtc.request.EndSessionRequestDTO;
import com.seesun.dto.webrtc.response.BootstrapResponseDTO;
import com.seesun.dto.webrtc.response.RecordingResponseDTO;
import com.seesun.security.userdetail.CustomUserDetails;
import com.seesun.service.webrtc.JanusRoomService;
import com.seesun.service.webrtc.RealtimeSessionService;
import com.seesun.service.webrtc.SseEmitterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.File;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seesun/live")
public class RealtimeSessionController {

    private final RealtimeSessionService realtimeSessionService;
    private final SseEmitterService sseEmitterService;
    private final JanusRoomService janusRoomService;

    @Value("${seesun.janus.base-url}")
    private String janusUrl;

    /**
     * MENTOR / MENTEE 공용 Bootstrap
     * role은 프론트에서 명시적으로 전달한다.
     */
    @PostMapping("/bootstrap/{id}")
    public BootstrapResponseDTO bootstrap(@AuthenticationPrincipal CustomUserDetails user,
    		@PathVariable("id") String uuid
    		) {
    	
    	return janusRoomService.ensureRoomExists(user.getMbId(), uuid);
    }


    // 테스트 용도(멘토 / 멘티 페이지 분리시)
//    @PostMapping("/join")
//    public BootstrapResponseDTO join(@RequestBody BootstrapRequestDTO req,
//                                     HttpServletRequest servletReq) {
//
//        long lectureId = req.getLectureId();
//
//        int roomId = janusRoomService.ensureRoomExists(lectureId);
//
//        String sessionId = UUIDUtil.generate();
//        String displayName = "mentee-" + lectureId + "-" + (int) (Math.random() * 99999);
//
//        return new BootstrapResponseDTO(
//                sessionId,
//                String.valueOf(roomId),
//                janusUrl,
//                "MENTEE",
//                displayName,
//                null
//        );
//    }

    @PostMapping("/end")
    public void endSession(@AuthenticationPrincipal CustomUserDetails user, @RequestBody EndSessionRequestDTO req) {
        Long memberId = user.getMbId();
        realtimeSessionService.endSession(req.getSessionId(), memberId);
    }

    // 녹화 관련
    @GetMapping("/recording")
    public RecordingResponseDTO recording(@RequestParam String sessionId) {
        return realtimeSessionService.getRecording(sessionId);
    }

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
    public SseEmitter sse(@AuthenticationPrincipal CustomUserDetails user, @RequestParam String sessionId) {
        Long memberId = user.getMbId();
        String clientKey = memberId + "-" + System.currentTimeMillis();
        return sseEmitterService.connect(sessionId, clientKey);
    }
}
