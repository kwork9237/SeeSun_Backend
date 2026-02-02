package com.seesun.controller.lecture;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.seesun.dto.lecture.LectureDTO;
import com.seesun.dto.lecture.LectureCreateDTO;
import com.seesun.service.lecture.LectureService;
import com.seesun.security.userdetail.CustomUserDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/lectures")
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;

    /**
     * 1. 강의 생성 API
     */
    @PostMapping
    public ResponseEntity<Long> createLecture(
            @RequestBody LectureCreateDTO createDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        log.info("강의 생성 요청 수신: {}", createDTO.getTitle());

        Long mbId = userDetails.getMbId();
        Long lectureId = lectureService.createLecture(createDTO, mbId);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(lectureId);
    }

    /**
     * 2. 강의 목록 조회 API
     * 모든 @RequestParam에 name을 명시하여 인식을 보장합니다.
     */
    @GetMapping
    public ResponseEntity<List<LectureDTO>> getLectureList(
            @RequestParam(name = "language", required = false) String language,
            @RequestParam(name = "difficulty", required = false) Integer difficulty,
            @RequestParam(name = "tags", required = false) String tags,
            @RequestParam(name = "timeSlot", required = false) String timeSlot,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "search", required = false) String search) {
        
        List<LectureDTO> lectures = lectureService.getLectureList(language, difficulty, tags, timeSlot, sortBy, search);
        return ResponseEntity.ok(lectures);
    }
    
    /**
     * 3. 강의 상세 조회 API
     * ✅ @PathVariable("id") 처럼 이름을 명시해야 에러가 발생하지 않습니다.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LectureDTO> getLectureDetail(@PathVariable("id") Long id) {
        LectureDTO lecture = lectureService.getLectureDetail(id);
        return ResponseEntity.ok(lecture);
    }
}