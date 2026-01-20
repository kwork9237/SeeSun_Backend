package com.seesun.controller.webrtc;
// controller를 WebSocket용으로 추가

import com.seesun.dto.webrtc.SignalMessageDTO;
import com.seesun.service.webrtc.WebRTCSignalService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebRTCStompController {
    private final WebRTCSignalService webRTCSignalService;

    // 프론트에서 stomClient.send("/pub/signal",...) 로 보내면 여기로 들어온다.
    @MessageMapping("signal")       // /pub/signal 로 온 메시지 받음
    public void signal(@Payload SignalMessageDTO dto) {
        // 실제 신호 처리 + 룸별 브로드캐스트는 서비스에서 처리
        System.out.println("[STOMP IN] type= " + dto.getType() + ", roomId = " + dto.getRoomId());
        webRTCSignalService.processSignal(dto);

    }
}
