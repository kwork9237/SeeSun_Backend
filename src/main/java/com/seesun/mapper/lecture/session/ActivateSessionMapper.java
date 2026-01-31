package com.seesun.mapper.lecture.session;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.seesun.dto.lecture.session.ActivateSessionDTO;

@Mapper
public interface ActivateSessionMapper {
	// 활성 세션 관리
	void insertSessionData(ActivateSessionDTO data);
	void deleteSessionData(@Param("room_id") short roomId);
	
	ActivateSessionDTO getSessionData(@Param("uuid") String uuid);
	int checkSessionByleId(@Param("le_id") Long leId);
}
