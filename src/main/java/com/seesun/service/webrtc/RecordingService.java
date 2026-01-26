package com.seesun.service.webrtc;


import com.seesun.global.exception.ErrorCode;
import com.seesun.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 작성자: 홍진기
 * 녹화 시작/종료/조회 로직을 JanusRecordingAdapter에 위임.
 * - Janus API와 통신하는 코드는 어댑터에서만 구현 (유지보수 용이)
 */
@Service
@RequiredArgsConstructor
public class RecordingService {

    private final JanusRecordingAdapter recordingAdapter;

    public void start(String sessionId, String roomId) {
        try {
            recordingAdapter.startRecording(sessionId, roomId);
        } catch (Exception e) {
            throw new GlobalException(ErrorCode.WEBRTC_RECORDING_FAILED);
        }
    }

    public void stop(String sessionId, String roomId) {
        try {
            recordingAdapter.stopRecording(sessionId, roomId);
        } catch (Exception e) {
            throw new GlobalException(ErrorCode.WEBRTC_RECORDING_FAILED);
        }
    }

    public String getRecordingUrlIfReady(String sessionId) {
        return recordingAdapter.getRecordingUrlIfReady(sessionId);
    }


}
