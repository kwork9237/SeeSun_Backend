package dto.lecture;

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
    // 기본 정보
    private Long leId;
    private String title;
    private String content;
    private String categoryName;
    
    // 멘토 정보
    private Long mbId;
    private String instructorName;
    private String profileIcon;
    private Boolean isNativeSpeaker;  // 원어민 여부
    
    // 강의 통계
    private Integer cost;
    private Double avgScore;
    private Integer difficulty;
    private Integer viewCount;
    private Integer reviewCount;      // 리뷰 수 추가
    private Integer studentCount;     // 수강생 수
    
    // 추가 정보
    private List<String> tags;        // ["회화", "문법", "시험"]
    private Integer totalHours;       // 총 수업 시간 (분 단위)
    private String availableTime;     // "오전 9시~오후 5시"
    private String timeSlot;          // "morning", "afternoon", "evening"
    
    // 활성화 상태
    private Boolean isActive;
}