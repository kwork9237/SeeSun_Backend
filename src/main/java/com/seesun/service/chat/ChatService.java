package com.seesun.service.chat;

import com.seesun.dto.chat.ChatMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 작성자: 홍진기
 *
 * 실시간 강의 채팅용 SSE 관리 서비스
 * -------------------------------------------------------
 * 역할 정리:
 * 1) connect(lectureId)
 *    - 강의 ID별로 SSEEmitter를 생성하고 저장
 *
 * 2) broadcast(messageDTO)
 *    - 특정 강의방(lectureId)에 연결된 모든 SSEEmitter에 메시지 PUSH
 *
 * 3) emitter 제거(clean-up)
 *    - 연결 끊김/timeout/error 발생 시 자동 정리
 *
 * 특징:
 * - ConcurrentHashMap을 사용해 스레드 안정성 확보
 * - EmitterList 를 lectureId 별로 유지하여 강의방 단위 실시간 채팅 가능
 */

@Service
@RequiredArgsConstructor
public class ChatService {

    private final Map<Long, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public SseEmitter connect(Long roomId) {
        SseEmitter emitter = new SseEmitter(0L);

        emitters.computeIfAbsent(roomId, k -> new CopyOnWriteArrayList<>())
                .add(emitter);

        emitter.onCompletion(() -> emitters.get(roomId).remove(emitter));
        emitter.onTimeout(() -> emitters.get(roomId).remove(emitter));
        emitter.onError((e) -> emitters.get(roomId).remove(emitter));

        return emitter;
    }

    private void remove(Long lectureId, SseEmitter emitter) {
        List<SseEmitter> list = emitters.get(lectureId);
        if (list != null) list.remove(emitter);
    }

    public void broadcast(ChatMessageDTO dto) {
        List<SseEmitter> list = emitters.getOrDefault(dto.getRoomId(), new ArrayList<>());

        for (SseEmitter emitter : list) {
            try {
                emitter.send(SseEmitter.event()
                        .name("chat")
                        .data(dto));
            } catch (Exception e) {
                emitter.complete();
            }
        }
    }

//    public void broadcast(ChatMessageDTO msg) {
//        Long room = msg.getLectureId();
//        List<SseEmitter> list = emitters.get(room);
//
//        System.out.println("[CHAT] broadcast room=" + room
//                + " sender=" + msg.getSender()
//                + " role=" + msg.getRole()
//                + " text=" + msg.getText());
//
//        System.out.println("[CHAT] subscribers for room=" + room + " => "
//                + (list == null ? "null" : list.size()));
//
//        if (list == null || list.isEmpty()) {
//            System.out.println("[CHAT] no subscribers. emitters keys=" + emitters.keySet());
//            return;
//        }
//
//        for (SseEmitter emitter : list) {
//            try {
//                emitter.send(SseEmitter.event().name("chat").data(msg));
//                System.out.println("[CHAT] sent OK");
//            } catch (Exception e) {
//                System.out.println("[CHAT] send FAIL: " + e.getClass().getName() + " " + e.getMessage());
//                emitter.complete();
//            }
//        }
//    }

}
