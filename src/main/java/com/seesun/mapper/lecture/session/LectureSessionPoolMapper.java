package com.seesun.mapper.lecture.session;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.seesun.dto.lecture.session.LectureSessionPoolDTO;

@Mapper
public interface LectureSessionPoolMapper {
	// 세션 풀 데이터 get
	public LectureSessionPoolDTO getPoolData();
	
	// 세션 활성, 비활성 업데이트
	public void activateSessionByRoomId(@Param("room_id") short roomId);
	public void deactivateSessionByRoomId(@Param("room_id") short roomId);
}
