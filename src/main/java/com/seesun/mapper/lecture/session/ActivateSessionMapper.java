package com.seesun.mapper.lecture.session;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.seesun.dto.lecture.session.ActivateSessionDTO;

@Mapper
public interface ActivateSessionMapper {
	// 활성 세션 관리
	void insertSessionData(ActivateSessionDTO data);
	void deleteSessionData(@Param("room_id") short roomId);
	
	ActivateSessionDTO getSessionDataByUuid(@Param("uuid") String uuid);
	ActivateSessionDTO getSessionDataByLeId(@Param("le_id") Long leId);
	
	int checkExistsSessionByleId(@Param("le_id") Long leId);
	int checkSessionOwner(@Param("mb_id") Long mbId);
	
	void setActivateStatus(@Param("mb_id") Long mbId);
	int checkSessionState(@Param("le_id") Long leId);
}
