package com.seesun.dto.mypage;

import lombok.Data;

@Data
public class DashboardDTO {
    // 강의 기본 정보
    private Long leId;              // 강의 ID (이동용)
    private String title;           // 강의 제목
    private String mentorName;      // 멘토 이름

    // Case A: 스케줄이 있을 때 들어가는 값
    private String nextLectureDate; // YYYY.MM.DD
    private String nextLectureTime; // HH:MM

    // Case B: 스케줄이 없을 때 보여줄 대체 정보
    private int cost;
    private int difficultyLevel;
}
