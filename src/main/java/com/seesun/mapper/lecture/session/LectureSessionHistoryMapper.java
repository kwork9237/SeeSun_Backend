package com.seesun.mapper.lecture.session;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.seesun.dto.lecture.session.LectureSessionHistoryDTO;

@Mapper
public interface LectureSessionHistoryMapper {
	void insertSessionData(LectureSessionHistoryDTO data);
	void deactivateSession(@Param("history_id") Long histId);
}
