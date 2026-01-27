package com.seesun.service.webrtc;

import com.seesun.janus.JanusRestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class JanusRoomService {

    private final JanusRestClient janusRestClient;

    @Value("${seesun.janus.room-id-base}")
    private int roomIdBase;

    public JanusRoomService(JanusRestClient janusRestClient) {
        this.janusRestClient = janusRestClient;
    }

    /** lectureId 기반 roomId 생성 */
    public int resolveRoomId(long lectureId) {
        return roomIdBase + (int)lectureId;
    }

    /** 방 존재 확인 → 없으면 생성 */
    public int ensureRoomExists(long lectureId) {
        int roomId = resolveRoomId(lectureId);

        long sessionId = janusRestClient.createSession();
        long handleId = janusRestClient.attachPlugin(sessionId);

        // exists 체크
        Map<?, ?> existsRes = janusRestClient.sendMessage(sessionId, handleId,
                Map.of("request", "exists", "room", roomId)
        );

        if (!exists(existsRes)) {
            janusRestClient.sendMessage(sessionId, handleId, Map.of(
                    "request", "create",
                    "room", roomId,
                    "description", "SeeSun lecture " + lectureId,
                    "publishers", 10,
                    "permanent", false
            ));
        }

        return roomId;
    }

    private boolean exists(Map<?, ?> res) {
        try {
            Map<?, ?> plugindata = (Map<?, ?>) res.get("plugindata");
            Map<?, ?> data = (Map<?, ?>) plugindata.get("data");
            return (boolean) data.get("exists");
        } catch (Exception e) {
            return false;
        }
    }
}
