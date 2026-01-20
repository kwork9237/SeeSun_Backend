package com.seesun.service.webrtc;

// 임시 메모리 상태 클래스 파일
// 방 ID
// 참여자 목록만 관리
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class RoomState {
    private final String roomId;
    private final Set<String> participants = new HashSet<>();

    public RoomState(String roomId) {
        this.roomId = roomId;
    }

    public void join(String userId) {
        participants.add(userId);
    }

    public boolean contains(String userId) {
        return participants.contains(userId);
    }

}
