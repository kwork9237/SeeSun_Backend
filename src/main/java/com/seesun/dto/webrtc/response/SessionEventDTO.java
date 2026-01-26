package com.seesun.dto.webrtc.response;

import lombok.Builder;
import lombok.Getter;

/** 작성자: 홍진기
 * SSE로 전달되는 실시간 이벤트 DTO
 * type: SESSION_ENDED (멘토 종료시 전원에 push)
 */
@Getter
@Builder
public class SessionEventDTO {
    private String type;    // "SESSION_ENDED"
    private String sessionId;
}
