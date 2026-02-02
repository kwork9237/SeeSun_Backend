package com.seesun.dto.chat;

import lombok.*;

// 채팅 데이터 구조
/**
 * 작성자: 홍진기
 * 실시간 강의 채팅 메시지 DTO
 * -----------------------------------------------
 * - lectureId: 강의 고유 ID (채팅방 역할)
 * - sender: 보낸 사람 이름
 * - role: 멘토/멘티 구분 (mentor / mentee / system)
 * - text: 메시지 내용
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {
    private Long roomId;   // 방 번호
    private String sender;    // 닉네임
    private String role;      // mentor / mentee / system
    private String text;      // 채팅 본문
}
