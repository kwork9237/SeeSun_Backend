package com.seesun.dto.lecture; // 패키지명은 프로젝트 구조에 맞게 수정

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("mainLectureResponse") // MyBatis XML에서 resultType으로 사용
public class MainLectureResponseDTO {
	// 메인 페이지 인기 강의 조회용 DTO

	private Long le_id; // 강의 ID
	private String le_title; // 강의 제목
	private String mb_nickname; // 멘토 닉네임 (Member 테이블 조인)
	private double mentor_rate; // 멘토 평점 (Member 테이블)
	private int le_price; // 가격
	private String le_thumb; // 썸네일 URL (프로필 아이콘 등)
}