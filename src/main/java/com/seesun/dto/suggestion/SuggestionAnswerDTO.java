package com.seesun.dto.suggestion;

import lombok.Data;

@Data
public class SuggestionAnswerDTO {
    private Long sgId;      // 어떤 건의사항인지
    private Long mbId;      // 작성자(관리자) ID
    private String content; // 답변 내용
}