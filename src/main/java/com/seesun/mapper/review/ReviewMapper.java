package com.seesun.mapper.review;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.seesun.dto.review.ReviewDTO;

@Mapper
public interface ReviewMapper {

    // 1. 리뷰 저장 (ReviewDTO 내의 leId, mbId, score 필드 사용)
    void insertReview(ReviewDTO reviewDTO);

    // 2. 강의별 리뷰 목록 조회
    // XML의 #{leId}와 매칭되도록 @Param 이름을 수정하는 것이 가독성에 좋습니다.
    List<ReviewDTO> selectReviews(@Param("leId") Long leId, @Param("sort") String sort);

    // 3. 별점 통계 조회
    Map<String, Object> selectReviewStats(@Param("leId") Long leId);
}