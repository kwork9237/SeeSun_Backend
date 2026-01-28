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

    @Transactional
    public void askQuestion(LectureQnaDTO qnaDTO, Long currentMemberId) {
        qnaDTO.setMbId(currentMemberId); // 현재 로그인한 사용자 ID 세팅
        qnaMapper.insertQuestion(qnaDTO);
    }

    public List<LectureQnaDTO> getLectureQnas(Long leId) {
        return qnaMapper.getQnaListByLecture(leId);
    }
}