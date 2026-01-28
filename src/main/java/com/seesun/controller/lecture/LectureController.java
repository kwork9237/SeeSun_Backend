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
     * try-catch를 Service로 이동하여 코드가 매우 간결해졌습니다.
     */
    @PostMapping
    public ResponseEntity<Long> createLecture(
            @RequestBody LectureCreateDTO createDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        log.info("강의 생성 요청 수신: {}", createDTO.getTitle());
        
        // 멘토 ID는 컨트롤러에서 추출하여 서비스로 넘겨주는 것이 정석입니다.
        Long mbId = userDetails.getMbId();
        Long lectureId = lectureService.createLecture(createDTO, mbId);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(lectureId);
    }

    /**
     * 2. 강의 목록 조회 API (필터링 포함)
     * 응답 규격을 ResponseEntity로 감싸서 일관성을 유지합니다.
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
     */
    @GetMapping("/{id}")
    public ResponseEntity<LectureDTO> getLectureDetail(@PathVariable("id") Long id) {
        LectureDTO lecture = lectureService.getLectureDetail(id);
        return ResponseEntity.ok(lecture);
    }
}