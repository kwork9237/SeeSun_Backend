package com.seesun.dto.webrtc.response;


import lombok.Builder;
import lombok.Getter;
/** 작성자: 홍진기
 * 녹화 파일 상태 및 URL 조회 DTO.
 */

@Getter
@Builder
public class RecordingResponseDTO {
    private String status;  // "PROCESSING" | "READY" | NONE
    private String url;     // READY일 때만 존재
}
