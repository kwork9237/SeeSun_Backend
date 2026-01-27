package com.seesun.janus;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class JanusRestClient {

    private final WebClient webClient;

    @Value("${seesun.janus.base-url}")
    private String janusBaseUrl;

    @Value("${seesun.janus.videoroom-plugin}")
    private String videoRoomPlugin;

    public JanusRestClient(WebClient webClient) {
        this.webClient = webClient;
    }

    /** 세션 생성 */
    public long createSession() {
        Map<String, Object> req = Map.of(
                "janus", "create",
                "transaction", tx()
        );

        Map<?, ?> res = webClient.post()
                .uri(janusBaseUrl)
                .bodyValue(req)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        Map<?, ?> data = (Map<?, ?>) res.get("data");
        return ((Number) data.get("id")).longValue();
    }

    /** plugin attach */
    public long attachPlugin(long sessionId) {
        Map<String, Object> req = Map.of(
                "janus", "attach",
                "plugin", videoRoomPlugin,
                "transaction", tx()
        );

        Map<?, ?> res = webClient.post()
                .uri(janusBaseUrl + "/" + sessionId)
                .bodyValue(req)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        Map<?, ?> data = (Map<?, ?>) res.get("data");
        return ((Number) data.get("id")).longValue();
    }

    /** message 전송 */
    public Map<?, ?> sendMessage(long sessionId, long handleId, Map<String, Object> body) {
        Map<String, Object> req = Map.of(
                "janus", "message",
                "body", body,
                "transaction", tx()
        );

        return webClient.post()
                .uri(janusBaseUrl + "/" + sessionId + "/" + handleId)
                .bodyValue(req)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    private String tx() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
