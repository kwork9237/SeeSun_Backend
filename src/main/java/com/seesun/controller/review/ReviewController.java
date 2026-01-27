package com.seesun.controller.review;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.seesun.dto.review.ReviewDTO; // 실제 DTO 경로에 맞게 수정
import com.seesun.security.userdetail.CustomUserDetails;
import com.seesun.service.review.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 강의별 리뷰 목록 및 통계 조회
     * @param lectureId 강의 ID
     * @param sort 정렬 기준 (latest, high, low)
     */
    @GetMapping
    public ResponseEntity<?> getLectureReviews(
            @RequestParam Long lectureId,
            @RequestParam(defaultValue = "latest") String sort) {
        
        // 서비스에서 리뷰 리스트와 통계 데이터(평균, 별점별 개수)를 Map으로 묶어 반환
        Map<String, Object> reviewData = reviewService.getReviewData(lectureId, sort);
        return ResponseEntity.ok(reviewData);
    }

    /**
     * 리뷰 등록 (MENTEE 권한 전용)
     */
    @PostMapping
    public ResponseEntity<?> createReview(
            @RequestBody ReviewDTO reviewDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        if (userDetails == null) {
            return ResponseEntity.status(401).body("로그인이 필요한 서비스입니다.");
        }

        // 로그인한 유저의 ID를 DTO에 설정
        reviewDTO.setMbId(userDetails.getMbId());
        
        try {
            reviewService.saveReview(reviewDTO);
            return ResponseEntity.ok("리뷰가 성공적으로 등록되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("리뷰 등록 중 오류가 발생했습니다.");
        }
    }
}