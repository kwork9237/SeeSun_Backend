package com.seesun.controller.qna;

import com.seesun.dto.lecture.LectureQnaDTO;
import com.seesun.service.qna.LectureQnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lectures/qna")
@RequiredArgsConstructor
public class LectureQnaController {

    private final LectureQnaService qnaService;

    // 질문 등록
    @PostMapping
    public ResponseEntity<String> askQuestion(@RequestBody LectureQnaDTO qnaDTO) {
        qnaService.askQuestion(qnaDTO, qnaDTO.getMb_id());
        return ResponseEntity.ok("질문이 등록되었습니다.");
    }

    // 목록 조회
    @GetMapping("/{leId}")
    public ResponseEntity<List<LectureQnaDTO>> getQnas(@PathVariable("leId") Long leId) {
        return ResponseEntity.ok(qnaService.getLectureQnas(leId));
    }

    // 답변 등록 (추가됨)
    @PutMapping("/answer")
    public ResponseEntity<String> updateAnswer(@RequestBody LectureQnaDTO qnaDTO) {
        qnaService.registerAnswer(qnaDTO);
        return ResponseEntity.ok("답변이 등록되었습니다.");
    }
}