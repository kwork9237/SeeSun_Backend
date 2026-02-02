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

    public Map<String, Object> getReviewData(Long lectureId, String sort) {
        Map<String, Object> result = new HashMap<>();
        
        // 1. 리뷰 리스트
        List<ReviewDTO> reviews = reviewMapper.selectReviews(lectureId, sort);
        result.put("list", reviews);

        // 2. 통계 데이터
        Map<String, Object> stats = reviewMapper.selectReviewStats(lectureId);
        
        // DB 결과가 없거나 null일 때의 기본값 설정
        if (stats == null || stats.isEmpty()) {
            stats = new HashMap<>();
            stats.put("totalCount", 0);
            stats.put("avgScore", 0.0); // 👈 avgRating에서 avgScore로 변경
            stats.put("star5", 0);
            stats.put("star4", 0);
            stats.put("star3", 0);
            stats.put("star2", 0);
            stats.put("star1", 0);
        } else {
            // SQL 결과를 한 번 더 체크: null 방지
            if (stats.get("avgScore") == null) {
                stats.put("avgScore", 0.0);
            }
        }
        
        result.put("stats", stats);
        return result;
    }

    @Transactional
    public void saveReview(ReviewDTO reviewDTO) {
        if (reviewDTO.getScore() < 1 || reviewDTO.getScore() > 5) {
            throw new IllegalArgumentException("평점은 1점에서 5점 사이여야 합니다.");
        }
        reviewMapper.insertReview(reviewDTO);
    }
}