package com.seesun.dto.suggestion;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SuggestionDTO {
    private Long sgId;
    private Long mbId;
    private String title;
    private String content;
    private int viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    
    // [추가된 필드] 답변 관련
    private String answerContent;   // 답변 내용
    private LocalDateTime answerCreatedAt; // 답변 작성일
}