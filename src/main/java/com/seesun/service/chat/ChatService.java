package com.seesun.service.chat;

import com.seesun.dto.chat.ChatMessageDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final Map<Long, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public SseEmitter connect(Long roomId) {
    	// 제한시간 30분 추가
        SseEmitter emitter = new SseEmitter(30L * 60 * 1000);

        emitters.computeIfAbsent(roomId, k -> new CopyOnWriteArrayList<>())
                .add(emitter);

        emitter.onCompletion(() -> remove(roomId, emitter));
        emitter.onTimeout(() -> remove(roomId, emitter));
        emitter.onError((e) -> remove(roomId, emitter));
        
        // 최초 연결 확인
        try {
            emitter.send(SseEmitter.event().name("ping").data("connected"));
        } catch (Exception e) {
            remove(roomId, emitter);
            emitter.complete();
        }

        return emitter;
    }

    public void broadcast(ChatMessageDTO dto) {
        List<SseEmitter> list = emitters.getOrDefault(dto.getRoomId(), new ArrayList<>());
        log.info("[SSE] broadcast roomId={}, listeners={}", dto.getRoomId(), list.size());

        for (SseEmitter emitter : list) {
            try {
                emitter.send(
                		SseEmitter.event().name("chat").data(dto)
            		);
            } catch (Exception e) {
                emitter.complete();
                remove(dto.getRoomId(), emitter);
                
                e.printStackTrace();
            }
        }
    }
    
    private void remove(Long roomId, SseEmitter emitter) {
        List<SseEmitter> list = emitters.get(roomId);
        if (list == null) return;

        list.remove(emitter);
        if (list.isEmpty()) {
            emitters.remove(roomId);
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
