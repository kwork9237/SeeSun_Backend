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
public class LectureCreateDTO {
    // 기본 정보
    private String title;
    private String language;
    private Integer level;
    private String description;
    
    // 분리된 SectionDTO 사용
    private List<SectionDTO> sections;
    
    // 스케줄 및 가격
    private String startDate;
    private String endDate;
    private List<String> selectedDays;
    private String startTime;
    private String endTime;
    private Integer maxStudents;
    private Integer price;

    private List<String> generatedSlots; 
    
    private Long mbId; // 작성자 ID
    private Long leId; // DB 입력 후 반환받을 강의 ID
}