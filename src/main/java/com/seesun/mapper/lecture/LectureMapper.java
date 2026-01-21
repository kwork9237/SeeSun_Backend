package com.seesun.mapper.lecture;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.seesun.dto.lecture.LectureDTO;
import com.seesun.dto.lecture.MainLectureResponseDTO;

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
    
 // ================= [추가 항목 - 담당자: 김지민] =================

 	/**
 	 * 1. 메인 페이지: 언어별 개설된 강의 수 조회 반환: [{lg_type_id=1, count=5}, ...] 형태의 Map 리스트
 	 */
 	List<Map<String, Object>> selectLectureStats();

 	/**
 	 * 2. 메인 페이지: 언어별 인기 강의 TOP 3 조회
 	 * 
 	 * @param lg_type_id 언어 타입 ID (1:영어, 2:일어, 3:중국어)
 	 */
 	List<MainLectureResponseDTO> selectPopularLectures(@Param("lg_type_id") Long lg_type_id);
 	
 	// =========================================================
}