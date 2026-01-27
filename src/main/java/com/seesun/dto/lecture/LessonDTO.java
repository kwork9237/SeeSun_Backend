package com.seesun.dto.lecture;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Alias("lesson")
public class LessonDTO {
    private String id;        // 프론트엔드 식별용 UUID
    private Long lessonId;    // DB 저장 후 생성되는 PK (조회 시 사용)
    private String title;     // 레슨 제목
    private String duration;  // 강의 시간 (예: "15:00")
}