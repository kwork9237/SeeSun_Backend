package com.seesun.dto.suggestion;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SuggestionDTO {
    private Long sgId;          // sg_id (PK)
    private Long mbId;          // mb_id (작성자 PK)
    private String title;       // title
    private String content;     // content
    private int viewCount;      // view_count
    private LocalDateTime createdAt;  // created_at
    private LocalDateTime modifiedAt; // modified_at
    
    // (선택사항) 목록에 작성자 이름도 보여주려면 나중에 Member 테이블과 조인해서 이 필드에 담음
    // private String memberName; 
}