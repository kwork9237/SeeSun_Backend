package com.seesun.service.webrtc;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.seesun.dto.webrtc.SignalMessageDTO;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class WebRTCSignalService {

    private final SimpMessagingTemplate messagingTemplate;

    private final Map<String, RoomState> rooms = new ConcurrentHashMap<>();

    public void processSignal(SignalMessageDTO dto) {

        switch (dto.getType()) {
            case "join"   -> handleJoin(dto);
            case "offer"  -> sendMessage(dto);
            case "answer" -> sendMessage(dto);
            case "ice"    -> sendMessage(dto);
        }
    }

    private void handleJoin(SignalMessageDTO dto) {
        rooms.computeIfAbsent(dto.getRoomId(), RoomState::new)
                .join(dto.getSenderId().toString());
    }

    /**
     * STOMP로 상대에게 메시지 전달
     */
    private void sendMessage(SignalMessageDTO dto) {

        // receiverId 기준으로 roomId 채널에 전송
        String destination = "/sub/room/" + dto.getRoomId();
        System.out.println("[STOMP OUT] -> " + destination + " type= " + dto.getType());

        // WebSocket(SOCKJS)로 메시지를 그대로 보냄
        messagingTemplate.convertAndSend(destination, dto);
    }
}
