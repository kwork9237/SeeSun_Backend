package com.seesun.dto.lecture;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

import org.apache.ibatis.type.Alias;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Alias("section")
public class SectionDTO {
    private String id;             // 프론트엔드 식별용 UUID
    private Long sectionId;        // DB 저장 후 생성되는 PK
    private String title;          // 섹션 제목
    private List<LessonDTO> lessons; // 해당 섹션에 속한 레슨 리스트
}