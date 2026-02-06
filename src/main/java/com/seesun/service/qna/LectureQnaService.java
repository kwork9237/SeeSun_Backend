package com.seesun.service.qna;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.seesun.dto.lecture.LectureQnaDTO;
import com.seesun.mapper.qna.LectureQnaMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LectureQnaService {

    private final LectureQnaMapper qnaMapper;

    // 1. 질문 등록
    @Transactional
    public void askQuestion(LectureQnaDTO qnaDTO, Long currentMemberId) {
        qnaDTO.setMb_id(currentMemberId); 
        qnaMapper.insertQuestion(qnaDTO);
    }

    // 2. 질문 목록 조회 (XML 조인을 통해 mentor_id가 포함됨)
    public List<LectureQnaDTO> getLectureQnas(Long leId) {
        return qnaMapper.getQnaListByLecture(leId);
    }

    // 3. 멘토 답변 등록 (추가)
    @Transactional
    public void registerAnswer(LectureQnaDTO qnaDTO) {
        // qnaDTO 내부에 qna_id, answer, ans_mb_id가 담겨 있어야 함
        qnaMapper.updateAnswer(qnaDTO);
    }
}