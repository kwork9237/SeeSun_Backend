package com.seesun.controller.lecture;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.seesun.dto.lecture.LectureDTO;
import com.seesun.dto.lecture.LectureCreateDTO; // 아까 만든 DTO로 변경
import com.seesun.service.lecture.LectureService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/lectures")
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;

    // 강의 생성 API
    @PostMapping
    public ResponseEntity<?> createLecture(@RequestBody LectureCreateDTO createDTO) {
        log.info("강의 생성 요청 수신: {}", createDTO.getTitle());
        
        try {
            // 서비스의 메서드명이 createLecture이므로 이에 맞게 호출합니다.
            Long lectureId = lectureService.createLecture(createDTO);
            
            // 성공 시 생성된 강의 ID를 반환 (리액트에서 alert으로 띄울 값)
            return ResponseEntity.ok(lectureId);
        } catch (Exception e) {
            log.error("강의 생성 실패: ", e);
            return ResponseEntity.internalServerError().body("강의 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * 2. 강의 목록 조회 API
     * GET http://localhost:8080/api/lectures
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
    
    /**
     * 3. 강의 상세 조회 API
     * GET http://localhost:8080/api/lectures/{id}
     */
    @GetMapping("/{id}")
    public LectureDTO getLectureDetail(@PathVariable(value = "id") Long id) {
        return lectureService.getLectureDetail(id);
    }
}