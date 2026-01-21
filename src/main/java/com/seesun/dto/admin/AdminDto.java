package com.seesun.dto.admin;

import lombok.Data;

@Data
public class AdminDto {
	private int newMentorCount;       // 신규 멘토 신청 수
    private int reportedLectureCount; // 신고된 강의 수
    private int inquiryCount;         // 미처리 건의사항 수
}
