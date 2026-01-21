package com.seesun.controller.lecture;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seesun.dto.lecture.LectureDTO;
import com.seesun.dto.lecture.MainLectureResponseDTO;
import com.seesun.service.lecture.LectureService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/lectures")
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;

    /**
     * 강의 목록 조회 API
     * GET http://localhost:8080/api/lectures?language=영어&difficulty=1&tags=회화,문법&sortBy=rating
     */
    @GetMapping
    public List<LectureDTO> getLectureList(
            @RequestParam(value = "language", required = false) String language,
            @RequestParam(value = "difficulty", required = false) Integer difficulty,
            @RequestParam(value = "tags", required = false) String tags,
            @RequestParam(value = "timeSlot", required = false) String timeSlot,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "search", required = false) String search) {
        
        return lectureService.getLectureList(language, difficulty, tags, timeSlot, sortBy, search);
    }
    
 // ================= [추가 항목 - 담당자: 김지민] =================

 	/**
 	 * [NEW] 메인 페이지: 언어별 개설된 강의 수 조회 API GET /api/lectures/count 반환 예시: { "1": 5,
 	 * "2": 3, "3": 0 } (Key는 lg_type_id)
 	 */
 	@GetMapping("/count")
 	public ResponseEntity<Map<Long, Long>> getLectureCounts() {
 		return ResponseEntity.ok(lectureService.getLectureCounts());
 	}

 	/**
 	 * [NEW] 메인 페이지: 언어별 인기 강의 TOP 3 조회 API GET /api/lectures/popular?lgType=1 특징:
 	 * 멘토 평점 순 정렬, 3개 제한
 	 */
 	@GetMapping("/popular")
 	public ResponseEntity<List<MainLectureResponseDTO>> getPopularLectures(
 			@RequestParam(value = "lgType") Long lgType) {

 		// [수정] MainLectureResponse -> MainLectureResponseDTO로 변경
 		return ResponseEntity.ok(lectureService.getPopularLectures(lgType));
 	}

 	// =========================================================
    
    /**
     * 강의 상세 조회 API
     */
    @GetMapping("/{id}")
    public LectureDTO getLectureDetail(@PathVariable Long id) {
        return lectureService.getLectureDetail(id);
    }
}