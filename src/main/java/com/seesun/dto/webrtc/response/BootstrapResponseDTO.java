package com.seesun.dto.webrtc.response;

import lombok.Builder;
import lombok.Getter;

/** 작성자: 홍진기
 * 실시간 강의 세션 입장(API bootstrap) 응답 DTO.
 * 프론트는 이 정보를 바탕으로 Janus 접속을 수행한다.
 */

@Getter
@Builder
public class BootstrapResponseDTO {
    private String sessionId;       // UUID 문자열
    private String roomId;          // Janus videroom room number(문자열 처리)
    private String janusUrl;        // 제공받은 Janus URL
    private String role;            // "MENTOR" | "MENTEE"
    private String displayName;     // Janus display (멘토/멘티 닉네임)
    private String mentorDisplayName;   // 멘티가 “멘토 feed” 찾을 때 사용 (프론트가 이 값 활용) :contentReference[oaicite:8]{index=8}

    public BootstrapResponseDTO(
            String sessionId,
            String roomId,
            String janusUrl,
            String role,
            String displayName,
            String mentorDisplayName
    ) {
        this.sessionId = sessionId;
        this.roomId = roomId;
        this.janusUrl = janusUrl;
        this.role = role;
        this.displayName = displayName;
        this.mentorDisplayName = mentorDisplayName;
    }
}
