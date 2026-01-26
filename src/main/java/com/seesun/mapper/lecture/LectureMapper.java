package com.seesun.mapper.lecture;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.seesun.dto.lecture.*;

@Mapper
public interface LectureMapper {
    
    List<LectureDTO> getLectureList(
        @Param("language") String language,
        @Param("difficulty") Integer difficulty,
        @Param("tags") String tags,
        @Param("timeSlot") String timeSlot,
        @Param("sortBy") String sortBy,
        @Param("search") String search
    );
    
    LectureDTO getLectureDetail(@Param("id") Long id);
    
    // 강의 생성 - Map으로 받기
    void insertLecture(Map<String, Object> params);
    
    void insertSection(Map<String, Object> params);
    
    void insertLesson(
        @Param("sectionId") Long sectionId,
        @Param("title") String title,
        @Param("duration") Integer duration,
        @Param("orderNum") Integer orderNum
    );
    
    void insertSchedule(
        @Param("leId") Long leId,
        @Param("scheduleDate") String scheduleDate,
        @Param("startTime") String startTime,
        @Param("endTime") String endTime,
        @Param("maxStudents") Integer maxStudents
    );
}