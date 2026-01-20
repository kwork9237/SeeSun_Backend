package com.seesun.dto.webrtc;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignalMessageDTO {

    private String type;     // join / offer / answer / ice
    private String roomId;  // lecture_session.room_id
    // private Long leId;       // 강의 ID (세션 검증용)
    private String senderId;   // 멘토 또는 멘티 member_id
    private String receiverId;// 상대방 member_id (offer/answer/ice)
    private Object payload;  // SDP / ICE
}
