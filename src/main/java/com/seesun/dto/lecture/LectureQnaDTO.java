package com.seesun.dto.lecture;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter
public class LectureQnaDTO {
    private Long qna_id;          
    private Long le_id;           
    private Long mb_id;           
    private String title;
    private String content;
    private String answer;
    private Long ans_mb_id;       
    private Integer status;
    private String authorNickname; 
    private Long mentor_id;        // ★ 추가: 강의를 개설한 멘토 ID (프론트 권한 체크용)
    private LocalDateTime created_at;  
    private LocalDateTime modified_at; 
}