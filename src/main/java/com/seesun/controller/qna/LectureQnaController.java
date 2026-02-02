package com.seesun.controller.qna;

import com.seesun.dto.lecture.LectureQnaDTO;
import com.seesun.service.qna.LectureQnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lecture/qna")
@RequiredArgsConstructor
public class LectureQnaController {

    private final LectureQnaService qnaService;

    // 질문 등록
    @PostMapping
    public ResponseEntity<String> askQuestion(@RequestBody LectureQnaDTO qnaDTO) {
        qnaService.askQuestion(qnaDTO, qnaDTO.getMbId());
        return ResponseEntity.ok("질문이 등록되었습니다.");
    }

    // 특정 강의의 질문 목록 조회
    // ✅ @PathVariable에 ("leId") 이름을 명시해줍니다.
    @GetMapping("/{leId}")
    public ResponseEntity<List<LectureQnaDTO>> getQnas(@PathVariable("leId") Long leId) {
        return ResponseEntity.ok(qnaService.getLectureQnas(leId));
    }
}