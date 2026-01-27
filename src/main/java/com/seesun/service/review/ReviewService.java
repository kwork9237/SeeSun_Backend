package com.seesun.service.review;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seesun.dto.review.ReviewDTO;
import com.seesun.mapper.review.ReviewMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewMapper reviewMapper;

    /**
     * 리뷰 목록과 통계 데이터를 한 번에 조회
     */
    public Map<String, Object> getReviewData(Long lectureId, String sort) {
        Map<String, Object> result = new HashMap<>();

        // 1. 정렬된 리뷰 리스트 조회
        List<ReviewDTO> reviews = reviewMapper.selectReviews(lectureId, sort);
        result.put("list", reviews);

        // 2. 별점 통계(평균, 각 별점별 개수) 조회
        Map<String, Object> stats = reviewMapper.selectReviewStats(lectureId);
        
        // 데이터가 없을 경우를 대비한 기본값 처리
        if (stats == null) {
            stats = new HashMap<>();
            stats.put("totalCount", 0);
            stats.put("avgRating", 0.0);
            stats.put("star5", 0);
            stats.put("star4", 0);
            stats.put("star3", 0);
            stats.put("star2", 0);
            stats.put("star1", 0);
        }
        
        result.put("stats", stats);

        return result;
    }

    /**
     * 리뷰 저장
     */
    @Transactional
    public void saveReview(ReviewDTO reviewDTO) {
        // 평점 유효성 검사 (1~5점)
        if (reviewDTO.getRating() < 1 || reviewDTO.getRating() > 5) {
            throw new IllegalArgumentException("평점은 1점에서 5점 사이여야 합니다.");
        }
        
        reviewMapper.insertReview(reviewDTO);
    }
}