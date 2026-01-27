package com.seesun.dto.review;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    
    private Long id;            // 리뷰 고유 번호 (PK)
    private Long lectureId;    // 대상 강의 번호 (FK)
    private Long mbId;         // 작성자 번호 (FK)
    private int rating;        // 별점 (1~5)
    private String content;    // 리뷰 내용
    private LocalDateTime createdAt; // 작성 일시

    // 조인(Join)을 통해 가져올 추가 데이터
    private String nickname;   // 작성자 닉네임 (화면 표시용)
    private String profileImg; // 작성자 프로필 이미지 (선택 사항)
}