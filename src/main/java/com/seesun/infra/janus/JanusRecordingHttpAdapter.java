package com.seesun.infra.janus;

import com.seesun.service.webrtc.JanusRecordingAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JanusRecordingHttpAdapter implements JanusRecordingAdapter {

    @Value("${seesun.recording.dir:/var/janus/recordings}")
    private String recordingDir;

    @Value("${seesun.recording.public-url:/api/seesun/session/recordings/file}")
    private String publicUrl;

    @Value("${seesun.recording.convert.enabled:true}")
    private boolean convertEnabled;

    @Value("${seesun.recording.convert.janus-pp-rec-path:janus-pp-rec}")
    private String janusPpRecPath;


    private final RestTemplate restTemplate = new RestTemplate();
    private final String JANUS_RECORD_URL = "https://janus.jsflux.co.kr/janus";

    @Override
    public void startRecording(String sessionId, String roomId) {
        // ✅ 현재 구조: 프론트에서 videoroom configure(record:true)로 녹화를 켬
        // 백엔드는 "시작 요청"을 상태/log용으로만 둬도 OK
        System.out.println("[Recording] START requested session=" + sessionId + ", room=" + roomId);
    }

    @Override
    public void stopRecording(String sessionId, String roomId) {
        // TODO: Janus 녹화 종료 요청
        System.out.println("[Recording] STOP requested session=" + sessionId + ", room=" + roomId);
    }

    @Override
    public String getRecordingUrlIfReady(String sessionId) {
        // TODO: Janus Recordings floder 조회 또는 DB 저장 결과 조회
        // 아직 준비 중이면 null 반환
        // 1) mp4가 있으면 바로 READY
        File mp4 = new File(recordingDir, "lecture-" + sessionId + ".mp4");
        if (mp4.exists() && mp4.length() > 0) {
            return publicUrl + "?sessionId=" + sessionId;
        }

        // 2) mp4 없고 mjr 있으면(녹화는 끝났는데 변환 전일 수 있음) 변환 시도
        File mjr = new File(recordingDir, "lecture-" + sessionId + ".mjr");
        if (mjr.exists() && mjr.length() > 0) {
            tryConvert(mjr, mp4);
        }
        // 변환 후 재확인
        if (mp4.exists() && mp4.length() > 0) {
            return publicUrl + "?sessionId=" + sessionId;
        }

        // 3) 아직 준비 안됨
        return null;
    }

    private void tryConvert(File mjr, File mp4) {
        // 이미 변환 중/완료 방지: mp4 있으면 스킵
        if(mp4.exists() && mp4.length() > 0) return;

        try {
            ProcessBuilder pb = new ProcessBuilder(
                    janusPpRecPath,
                    mjr.getAbsolutePath(),
                    mp4.getAbsolutePath()
            );
            pb.redirectErrorStream(true);
            Process p = pb.start();
            int code = p.waitFor();
            System.out.println("[Recording] Convert result=" + code + " mjr=" + mjr.getName());

        } catch (IOException | InterruptedException e) {
            System.out.println("[Recording] Convert failed: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
