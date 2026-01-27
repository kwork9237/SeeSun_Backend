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

    // 1. 외부 파일로 분리한 DTO를 리스트로 참조
    private List<SectionDTO> sections; 

    // --- 기존 내부 클래스(SectionDTO, LessonDTO) 삭제됨 ---

    private Long leId;
    private String title;
    private String content;
    private String categoryName;

    // 멘토 정보 (상세 페이지 노출용)
    private Long mbId;
    private String instructorName;
    private String profileIcon;
    private Boolean isNativeSpeaker;

    // 강의 통계 및 가격 (조회 시에만 필요한 필드들)
    private Integer cost;
    private Double avgScore;
    private Integer difficulty;
    private Integer viewCount;
    private Integer reviewCount;
    private Integer studentCount;

    // 시간 및 태그 정보
    private List<String> tags;
    private Double totalHours;       
    private String availableTime;     
    private String timeSlot;

    // 날짜 및 스케줄 정보
    private String startDate;         
    private String endDate;           
    private String availableDays;     

    private Boolean isActive;

    private Long scheduleId;         // 스케줄 고유 ID
    private String scheduleDate;     // 날짜 (예: "2026-01-24")
    private String startTime;        // 시작 시간 (예: "14:00")
    private String endTime;          // 종료 시간 (예: "15:00")
    private Integer maxStudents;     // 정원 (스케줄별)
    private Integer currentStudents; // 현재 신청 인원 (스케줄별)

    private String modifiedAt;    // String으로 바꿨는지 확인!
    private String progressStatus; // 이거 추가했는지 확인!
}