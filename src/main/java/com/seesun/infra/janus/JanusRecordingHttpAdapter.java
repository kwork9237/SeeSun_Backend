package com.seesun.infra.janus;

import com.seesun.service.webrtc.JanusRecordingAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class JanusRecordingHttpAdapter implements JanusRecordingAdapter {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String JANUS_RECORD_URL = "https://janus.jsflux.co.kr/janus";

    @Override
    public void startRecording(String sessionId, String roomId) {
        // TODO: Janus video-room plugin 통해 녹화 시작 요청
        System.out.println("Start Recording: session= " + sessionId + ", room=" + roomId);
    }

    @Override
    public void stopRecording(String sessionId, String roomId) {
        // TODO: Janus 녹화 종료 요청
        System.out.println("Stop Recording: session= " + sessionId + ", room=" + roomId);
    }

    @Override
    public String getRecordingUrlIfReady(String sessionId) {
        // TODO: Janus Recordings floder 조회 또는 DB 저장 결과 조회
        // 아직 준비 중이면 null 반환
        return null;
    }
}
