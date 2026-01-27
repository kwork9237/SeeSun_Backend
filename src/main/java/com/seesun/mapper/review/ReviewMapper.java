package com.seesun.mapper.review;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.seesun.dto.review.ReviewDTO;

@Mapper
public interface ReviewMapper {

    // 1. 리뷰 저장
    void insertReview(ReviewDTO reviewDTO);

    // 2. 강의별 리뷰 목록 조회 (정렬 파라미터 포함)
    // sort 값: 'latest' (최신순), 'high' (평점 높은순), 'low' (평점 낮은순)
    List<ReviewDTO> selectReviews(@Param("lectureId") Long lectureId, @Param("sort") String sort);

    // 3. 별점 통계 조회 (평균 및 각 점수대별 인원수)
    // 결과값은 Map<String, Object>로 받아 서비스에서 처리
    Map<String, Object> selectReviewStats(Long lectureId);
}