package com.seesun.controller.webrtc;

import com.seesun.dto.webrtc.SignalMessageDTO;
import com.seesun.service.webrtc.WebRTCSignalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/webrtc")
public class WebRTCSignalController {
    private final WebRTCSignalService webRTCSignalService;

    @PostMapping("signal")
    public void signal(@RequestBody SignalMessageDTO dto) {
        webRTCSignalService.processSignal(dto);
    }
}
