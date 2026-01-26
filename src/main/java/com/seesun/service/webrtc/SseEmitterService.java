package com.seesun.service.webrtc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seesun.dto.webrtc.response.SessionEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 작성자: 홍진기
 * SSE 연결 및 브로드캐스트 관리 서비스
 * - 각 sessionId마다 연결된 모든 클라이언트에게 이벤트 push
 * - 실시간 강의 종료시 멘토 종료 이벤트를 전원에게 전달함
 */

@Service
@RequiredArgsConstructor
public class SseEmitterService {
    private final Map<String, Map<String, SseEmitter>> repository = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 클라이언트를 sessinId 기반으로 SSE 연결
     * @param sessionId
     * @param clientKey
     * @return
     */
    public SseEmitter connect(String sessionId, String clientKey) {
        SseEmitter emitter = new SseEmitter(0L);

        repository.computeIfAbsent(sessionId, k -> new ConcurrentHashMap<>())
                  .put(clientKey, emitter);

        // 정리 로직
        emitter.onCompletion(() -> remove(sessionId, clientKey));
        emitter.onTimeout(() -> remove(sessionId, clientKey));
        emitter.onError(e -> remove(sessionId, clientKey));

        return emitter;
    }

    /**
     * 특정 세션의 모든 클라이언트에게 이벤트 전송
     * @param sessionId
     * @param event
     */
    public void broadcast(String sessionId, SessionEventDTO event) {
        Map<String, SseEmitter> emitters = repository.get(sessionId);
        if (emitters == null) return;

        emitters.forEach((key, emitter) -> {
           try {
               String json = objectMapper.writeValueAsString(event);
               emitter.send(SseEmitter.event().name("message").data(json));
           }catch (Exception e) {
               remove(sessionId, key);
           }
        });
    }

    /**
     * emitter 정리
     * @param sessionId
     * @param clientKey
     */
    private void remove(String sessionId, String clientKey) {
        Map<String, SseEmitter> map = repository.get(sessionId);
        if (map == null) return;
        map.remove(clientKey);
        if (map.isEmpty()) repository.remove(sessionId);
    }
}
