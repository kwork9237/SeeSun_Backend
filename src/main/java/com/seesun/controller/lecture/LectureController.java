package com.seesun.controller.lecture;

import java.util.List;
import org.springframework.web.bind.annotation.*;

import com.seesun.service.lecture.LectureService;
import dto.lecture.LectureDTO;
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
    
    /**
     * 강의 상세 조회 API
     */
    @GetMapping("/{id}")
    public LectureDTO getLectureDetail(@PathVariable Long id) {
        return lectureService.getLectureDetail(id);
    }
}