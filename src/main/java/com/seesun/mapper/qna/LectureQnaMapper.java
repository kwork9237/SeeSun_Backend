package com.seesun.mapper.qna;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.seesun.dto.lecture.LectureQnaDTO;

@Mapper
public interface LectureQnaMapper {
    // 1. 질문 저장
    void insertQuestion(LectureQnaDTO qnaDTO);

    // 2. 특정 강의의 질문 목록 조회
    List<LectureQnaDTO> getQnaListByLecture(Long leId);

    // 3. 답변 등록/수정 (DTO 객체를 넘기도록 수정하여 XML과 일치시킴)
    void updateAnswer(LectureQnaDTO qnaDTO);
}