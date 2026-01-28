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
	
	// SQL에서 연산/가공되어 넘어오는 필드 (AS 별칭 필수)
	private int total_hours; // 총 수업 시간 (DB에서 시작/종료 시간 차이 계산 또는 슬롯 수 합계)
	private String available_time; // 강의 가능 요일/시간 요약 (UI 표시용 문자열, 예: "월,수 16:00~18:00")
}