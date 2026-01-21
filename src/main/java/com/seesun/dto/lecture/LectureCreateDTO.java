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
    
    // Step 1: 기본 정보
    private String title;
    private String language;      // "en", "jp", "cn"
    private Integer level;        // 1, 2, 3
    private String description;
    
    // Step 2: 커리큘럼
    private List<SectionDTO> sections;
    
    // Step 3: 스케줄 & 가격
    private String startDate;
    private String endDate;
    private List<String> selectedDays;
    private String startTime;
    private String endTime;
    private Integer maxStudents;
    private List<String> generatedSlots;
    private Integer price;
    
    // 멘토 ID (로그인한 사용자)
    private Long mbId;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SectionDTO {
        private String id;  // UUID (프론트엔드용)
        private String title;
        private List<LessonDTO> lessons;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LessonDTO {
        private String id;  // UUID (프론트엔드용)
        private String title;
        private String duration;  // 프론트에서 String으로 오므로
    }
}
