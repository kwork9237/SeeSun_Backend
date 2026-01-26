package com.seesun.dto.lecture;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LectureDTO {

    private List<SectionDTO> sections; // 상세 조회 시 커리큘럼 목록을 포함

    @Data
    public static class SectionDTO {
        private Long sectionId;
        private String title;
        private List<LessonDTO> lessons;
    }

    @Data
    public static class LessonDTO {
        private Long lessonId;
        private String title;
        private Integer duration;
    }
    
    private Long leId;
    private String title;
    private String content;
    private String categoryName;

    // 멘토 정보
    private Long mbId;
    private String instructorName;
    private String profileIcon;
    private Boolean isNativeSpeaker;

    // 강의 통계 및 가격
    private Integer cost;
    private Double avgScore;
    private Integer difficulty;
    private Integer viewCount;
    private Integer reviewCount;
    private Integer studentCount;

    // 시간 및 태그 정보
    private List<String> tags;
    private Double totalHours;       // 계산된 시간 단위 (예: 1.5, 2.0)
    private String availableTime;     // 포맷팅된 시간 (예: "12:00 ~ 15:00")
    private String timeSlot;

    // ✅ 추가: 날짜 및 스케줄 정보
    private String startDate;         // 강의 시작일 "2026-01-01"
    private String endDate;           // 강의 종료일 "2026-12-31"
    private String availableDays;     // 수업 가능한 요일 "0,1,2" (일,월,화)
    // availableTime은 이미 있으므로 용도 변경: "09:00,14:00,19:00" 형태로 사용

    private Boolean isActive;
}