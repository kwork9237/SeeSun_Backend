package com.seesun.controller.review;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DuplicateKeyException; // 👈 추가

import com.seesun.dto.review.ReviewDTO;
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
     */
    @GetMapping
    public ResponseEntity<?> getLectureReviews(
            @RequestParam(name = "leId") Long lectureId, // 👈 프론트의 leId와 명칭 확인 필요
            @RequestParam(name = "sort", defaultValue = "latest") String sort) {
        
        Map<String, Object> reviewData = reviewService.getReviewData(lectureId, sort);
        return ResponseEntity.ok(reviewData);
    }

    /**
     * 리뷰 등록 (중복 작성 방지 포함)
     */
    @PostMapping
    public ResponseEntity<?> createReview(
            @RequestBody ReviewDTO reviewDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        if (userDetails == null) {
            return ResponseEntity.status(401).body("로그인이 필요한 서비스입니다.");
        }

        reviewDTO.setMbId(userDetails.getMbId());
        
        try {
            reviewService.saveReview(reviewDTO);
            return ResponseEntity.ok("리뷰가 성공적으로 등록되었습니다.");
        } catch (DuplicateKeyException e) {
            // 👈 DB의 Unique 제약 조건 위반 시 실행 (이미 작성한 경우)
            return ResponseEntity.status(409).body("이미 해당 강의에 대한 리뷰를 작성하셨습니다.");
        } catch (Exception e) {
            e.printStackTrace(); 
            return ResponseEntity.status(500).body("리뷰 등록 중 오류가 발생했습니다.");
        }
    }
}