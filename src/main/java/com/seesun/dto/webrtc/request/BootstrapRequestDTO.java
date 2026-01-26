package com.seesun.dto.webrtc.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

/* 작성자: 홍진기
* 실시간 강의 세션 입장(API: bootstrap) 요청 DTO.
* - lectureID: 입장하려는 강의 ID
*/

@Getter
@NoArgsConstructor
public class BootstrapRequestDTO {
    private long lectureId;
}
