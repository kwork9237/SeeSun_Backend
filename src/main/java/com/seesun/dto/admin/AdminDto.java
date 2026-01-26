package com.seesun.dto.admin;

import lombok.Data;

// 관리자 초기 화면 DTO
@Data
public class AdminDto {
	private int newMentorCount;       // 신규 멘토 신청 수
    private int reportedLectureCount; // 신고된 강의 수
    private int inquiryCount;         // 미처리 건의사항 수
}
