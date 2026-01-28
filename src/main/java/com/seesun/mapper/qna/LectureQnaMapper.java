package com.seesun.mapper.qna;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.seesun.dto.lecture.LectureQnaDTO;

@Mapper
public interface LectureQnaMapper {

    // 1. 질문 저장 (XML의 id="insertQuestion"과 매칭)
    void insertQuestion(LectureQnaDTO qnaDTO);

    // 2. 특정 강의의 질문 목록 조회 (XML의 id="getQnaListByLecture"과 매칭)
    List<LectureQnaDTO> getQnaListByLecture(Long leId);

    // 3. 답변 등록/수정 (XML의 id="updateAnswer"과 매칭)
    // 파라미터가 여러 개일 때는 @Param을 써주는 것이 안전합니다.
    void updateAnswer(@Param("qna_id") Long qnaId, 
                      @Param("answer") String answer, 
                      @Param("ansMbId") Long ansMbId);
}