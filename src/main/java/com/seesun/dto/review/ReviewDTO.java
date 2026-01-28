package com.seesun.dto.review;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private Long leEvalId;   // DB: le_eval_id (PK)
    private Long leId;       // DB: le_id (강의 ID)
    private Long mbId;       // DB: mb_id (회원 ID)
    private int score;       // DB: score (별점, 기존 rating에서 변경)
    private String content;  // DB: content (리뷰 내용)
    private LocalDateTime createdAt;

    // 조인(Join)을 통해 가져올 추가 데이터
    private String nickname;   // 작성자 닉네임 (화면 표시용)
    private String profileImg; // 작성자 프로필 이미지 (선택 사항)
}