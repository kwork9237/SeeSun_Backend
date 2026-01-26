package com.seesun.dto.webrtc.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

/** 작성자: 홍진기
 * 멘토가 강의를 종료할 때 사용하는 요청 DTO
 */

@Getter
@NoArgsConstructor
public class EndSessionRequestDTO {
    private String sessionId;
}
