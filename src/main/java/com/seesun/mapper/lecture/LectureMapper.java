package com.seesun.mapper.lecture;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.seesun.dto.lecture.*;

@Mapper
public interface LectureMapper {
    
    // 강의 목록 조회
    List<LectureDTO> getLectureList(
        @Param("language") String language,
        @Param("difficulty") Integer difficulty,
        @Param("tags") String tags,
        @Param("timeSlot") String timeSlot,
        @Param("sortBy") String sortBy,
        @Param("search") String search
    );
    
    // 강의 상세 정보(기본) 조회
    LectureDTO getLectureDetail(@Param("id") Long id);

    // ✅ 추가: 특정 강의에 속한 모든 섹션 조회
    List<LectureDTO.SectionDTO> getSectionsByLectureId(@Param("leId") Long leId);

    // ✅ 추가: 특정 섹션에 속한 모든 레슨 조회
    List<LectureDTO.LessonDTO> getLessonsBySectionId(@Param("sectionId") Long sectionId);
    
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