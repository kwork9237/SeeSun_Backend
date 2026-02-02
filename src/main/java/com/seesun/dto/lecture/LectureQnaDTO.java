package com.seesun.dto.lecture;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter
public class LectureQnaDTO {
    private Long qnaId;
    private Long leId;
    private Long mbId;        // DB 타입인 BIGINT에 맞춰 Long으로 변경
    private String title;
    private String content;
    private String answer;
    private Long ansMbId;     // 답변자 멘토 ID
    private Integer status;
    private String authorNickname; // 목록 조회 시 표시용
    private LocalDateTime createdAt;
    private LocalDateTime modified_at;
}