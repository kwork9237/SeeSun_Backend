package com.seesun.repository.webrtc;

import com.seesun.domain.webrtc.RealtimeSession;
import com.seesun.domain.webrtc.SessionStatus;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 작성자: 홍진기
 * RealtimeSession을 저장하는 InMemory 저장소
 * 개발 단계에서 사용하며,
 * 실제 운영에서는 JPA/MyBatis로 대체가 가능하다.
 */

@Repository
public class InMemoryRealtimeSessionRepository {

    private final Map<String, RealtimeSession> map = new ConcurrentHashMap<>();

    public Optional<RealtimeSession> findBySessionId(String sessionId) {
        return Optional.ofNullable(map.get(sessionId));
    }

    public Optional<RealtimeSession> findActiveByLectureId(Long lectureId) {
        return map.values().stream()
                           .filter(s -> s.getLectureId().equals(lectureId))
                           .filter(s -> s.getStatus() == SessionStatus.ACTIVE)
                           .findFirst();
    }

    public void save(RealtimeSession session) {
        map.put(session.getSessionId(), session);
    }

    public void update(RealtimeSession session) {
        map.put(session.getSessionId(), session);
    }
}
