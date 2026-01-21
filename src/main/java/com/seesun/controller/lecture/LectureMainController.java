package com.seesun.controller.lecture;

import com.seesun.dto.lecture.MainLectureResponseDTO;
import com.seesun.service.lecture.LectureMainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lectures")
@RequiredArgsConstructor
public class LectureMainController {

    private final LectureMainService lectureMainService;

    /**
     * 메인 페이지: 언어별 개설된 강의 수 조회
     */
    @GetMapping("/count")
    public ResponseEntity<Map<Long, Long>> getLectureCounts() {
        return ResponseEntity.ok(lectureMainService.getLectureCounts());
    }

    /**
     * 메인 페이지: 언어별 인기 강의 TOP 3 조회
     */
    @GetMapping("/popular")
    public ResponseEntity<List<MainLectureResponseDTO>> getPopularLectures(
            @RequestParam(value = "lgType") Long lgType) {
        return ResponseEntity.ok(lectureMainService.getPopularLectures(lgType));
    }
}