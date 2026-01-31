package com.seesun.mapper.lecture;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.seesun.dto.lecture.*;

@Mapper
public interface LectureMapper {
    
    // 1. 강의 목록 조회
    List<LectureDTO> getLectureList(
        @Param("language") String language,
        @Param("difficulty") Integer difficulty,
        @Param("tags") String tags,
        @Param("timeSlot") String timeSlot,
        @Param("sortBy") String sortBy,
        @Param("search") String search
    );
    
    // 2. 강의 상세 정보 조회
    LectureDTO getLectureDetail(@Param("id") Long id);

    // 3. 상세 조회용 커리큘럼 로직 (분리된 DTO 클래스 사용)
    List<SectionDTO> getSectionsByLectureId(@Param("leId") Long leId);
    List<LessonDTO> getLessonsBySectionId(@Param("sectionId") Long sectionId);
    
    // 4. 강의 생성 (Map 대신 DTO와 전용 파라미터 사용)
    void insertLecture(@Param("dto") LectureCreateDTO dto, @Param("lgTypeId") short lgTypeId);
    
    // 5. 섹션 생성 (개별 파라미터 매핑)
    void insertSection(@Param("leId") Long leId, @Param("section") SectionDTO section, @Param("orderNum") int orderNum);
    
    // 6. 레슨 생성
    void insertLesson(
        @Param("sectionId") Long sectionId,
        @Param("title") String title,
        @Param("duration") Integer duration,
        @Param("orderNum") Integer orderNum
    );
    
    // 7. 스케줄 생성
    void insertSchedule(
        @Param("leId") Long leId,
        @Param("scheduleDate") String scheduleDate,
        @Param("startTime") String startTime,
        @Param("endTime") String endTime,
        @Param("maxStudents") Integer maxStudents
    );
    
    int checkLectureMember(@Param("mb_id") Long mbId, @Param("le_id") Long leId);
}