package com.seesun.controller.webrtc;

import com.seesun.dto.webrtc.request.BootstrapRequestDTO;
import com.seesun.dto.webrtc.request.EndSessionRequestDTO;
import com.seesun.dto.webrtc.response.BootstrapResponseDTO;
import com.seesun.dto.webrtc.response.RecordingResponseDTO;
import com.seesun.global.uuid.UUIDUtil;
import com.seesun.security.userdetail.CustomUserDetails;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.File;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seesun/janus")
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
    @PostMapping("/bootstrap")
    public BootstrapResponseDTO bootstrap(@AuthenticationPrincipal CustomUserDetails user) {
    	
    	
    	
    	return null;
    }
    
    
    
//    @PostMapping("/bootstrap")
//    public BootstrapResponseDTO bootstrap(
//            @RequestBody BootstrapRequestDTO req
//    ) {
//        long lectureId = req.getLectureId();
//        int roomId = janusRoomService.ensureRoomExists(lectureId);
//
//        // 프론트가 role을 보내도록 한다.
//        String role = (req.getRole() == null) ? "MENTEE" : req.getRole().toUpperCase();
//
//        String sessionId = UUIDUtil.generate();
//        String displayName;
//
//        if (role.equals("MENTOR")) {
//            displayName = "mentor-" + lectureId + "-" + (int)(Math.random() * 99999);
//        } else {
//            displayName = "mentee-" + lectureId + "-" + (int)(Math.random() * 99999);
//        }
//
//        return new BootstrapResponseDTO(
//                sessionId,
//                String.valueOf(roomId),
//                janusUrl,
//                role,
//                displayName,
//                role.equals("MENTOR") ? displayName : null
//        );
//    }

    // 테스트 용도(멘토 / 멘티 페이지 분리시)
    @PostMapping("/join")
    public BootstrapResponseDTO join(@RequestBody BootstrapRequestDTO req,
                                     HttpServletRequest servletReq) {

        long lectureId = req.getLectureId();

        int roomId = janusRoomService.ensureRoomExists(lectureId);

        String sessionId = UUIDUtil.generate();
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
    public SseEmitter sse(@RequestParam String sessionId) {
        Long memberId = getLoginMemberId();
        String clientKey = memberId + "-" + System.currentTimeMillis();
        return sseEmitterService.connect(sessionId, clientKey);
    }

    private Long getLoginMemberId() {
        return 1L; // TODO: Authentication 적용시 수정
    }
}
