package com.seesun.service.lecture.session;

import org.springframework.stereotype.Service;

import com.seesun.dto.lecture.session.LectureSessionPoolDTO;
import com.seesun.mapper.lecture.session.LectureSessionPoolMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LectureSessionPoolSerivce {
	private final LectureSessionPoolMapper sessionPoolMapper;
	
	public LectureSessionPoolDTO getPoolData() {
		return sessionPoolMapper.getPoolData();
	}
	
	public void activateSessionByRoomId(short roomId) {
		sessionPoolMapper.activateSessionByRoomId(roomId);
	}
	
	public void deactivateSessionByRoomId(short roomId) {
		sessionPoolMapper.deactivateSessionByRoomId(roomId);
	}
}
