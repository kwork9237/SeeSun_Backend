package com.seesun.service.webrtc;

/**
 * Janus 서버와의 연동을 추상화한 인터페이스.
 * - startRecording / stopRecording / getRecordingUrlIfReady
 *
 * 실제 구현은 Janus 서버가 제공하는 API 스펙에 따라 작성한다.
 */
public interface JanusRecordingAdapter {

    void startRecording(String sessionId, String roomId);

    void stopRecording(String sessionId, String roomId);

    /**
     * 녹화 준비가 완료되면 URL 반환.
     * 아직 준비되지 않았다면 null 반환.
     */
    String getRecordingUrlIfReady(String sessionId);
}