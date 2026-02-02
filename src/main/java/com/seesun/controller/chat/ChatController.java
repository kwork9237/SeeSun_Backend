package com.seesun.controller.chat;


import com.seesun.dto.chat.ChatMessageDTO;
import com.seesun.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * 실시간 강의 채팅 컨트롤러
 * -------------------------------------------------------
 * SSE 스트림 + 메시지 전송 담당
 *
 * 엔드포인트 정리:
 *
 * 1) GET /api/seesun/session/chat/stream?lectureId=123
 *    - SSE 연결 (클라이언트가 채팅을 실시간으로 받는 통로)
 *
 * 2) POST /api/seesun/session/chat/send
 *    - 메시지 전송 (멘토/멘티가 채팅 입력 시 호출)
 */
@RestController
@RequestMapping("/api/seesun/session/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/send")
    public ResponseEntity<Void> send(@RequestBody ChatMessageDTO dto) {
        System.out.println("[CHAT] /send called: " + dto.getRoomId() + " / " + dto.getText());
        chatService.broadcast(dto);
        return ResponseEntity.ok().build();
    }


    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@RequestParam Long roomId) {
        System.out.println("[SSE] new subscriber for lectureId = " + roomId);
        return chatService.connect(roomId);
    }
}
