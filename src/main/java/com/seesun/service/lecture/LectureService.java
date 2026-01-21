package com.seesun.service.lecture;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seesun.dto.lecture.LectureDTO;
import com.seesun.dto.lecture.MainLectureResponseDTO;
import com.seesun.mapper.lecture.LectureMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LectureService {
    private final LectureMapper lectureMapper;

    public List<LectureDTO> getLectureList(
            String language, 
            Integer difficulty, 
            String tags, 
            String timeSlot, 
            String sortBy, 
            String search) {
        
        // sortBy 기본값 설정
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "rating";
        }
        
        return lectureMapper.getLectureList(language, difficulty, tags, timeSlot, sortBy, search);
    }
    
    public LectureDTO getLectureDetail(Long id) {
        return lectureMapper.getLectureDetail(id);
    }
    
 // ================= [추가 항목 - 담당자: 김지민] =================

 	/**
 	 * 1. 메인 페이지: 언어별 개설된 강의 수 조회 DB에서 List<Map> 형태로 받은 데이터를 프론트엔드가 쓰기 편한 Map<Long,
 	 * Long> 형태로 변환합니다. 예: [{lg_type_id=1, count=5}] -> { 1: 5, 2: 0, 3: 0 }
 	 */
 	@Transactional(readOnly = true)
 	public Map<Long, Long> getLectureCounts() {
 		List<Map<String, Object>> resultList = lectureMapper.selectLectureStats();

 		Map<Long, Long> stats = new HashMap<>();
 		// 초기값 설정 (데이터가 없는 언어도 0으로 표시하기 위함)
 		stats.put(1L, 0L); // 영어
 		stats.put(2L, 0L); // 일어
 		stats.put(3L, 0L); // 중국어

 		for (Map<String, Object> row : resultList) {
 			// DB 드라이버에 따라 리턴 타입이 다를 수 있어 안전하게 형변환
 			Long typeId = ((Number) row.get("lg_type_id")).longValue();
 			Long count = ((Number) row.get("count")).longValue();
 			stats.put(typeId, count);
 		}

 		return stats;
 	}

 	/**
 	 * 2. 메인 페이지: 언어별 인기 강의 TOP 3 조회 해당 언어(lgType)의 멘토 평점 상위 3개 강의를 가져옵니다.
 	 */
 	@Transactional(readOnly = true)
 	public List<MainLectureResponseDTO> getPopularLectures(Long lgType) {
 		return lectureMapper.selectPopularLectures(lgType);
 	}
 	
 	// =========================================================
}