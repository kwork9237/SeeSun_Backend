package com.seesun.service.lecture;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seesun.mapper.lecture.LectureMapper;
import com.seesun.dto.lecture.*;
import lombok.RequiredArgsConstructor;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LectureService {
    private final LectureMapper lectureMapper;

    // 강의 목록 조회
    public List<LectureDTO> getLectureList(
            String language, 
            Integer difficulty, 
            String tags, 
            String timeSlot, 
            String sortBy, 
            String search) {
        
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "rating";
        }
        
        return lectureMapper.getLectureList(language, difficulty, tags, timeSlot, sortBy, search);
    }
    
    // ✅ 상세 조회: 강의 + 섹션 + 레슨 데이터를 조립하여 반환
    public LectureDTO getLectureDetail(Long id) {
        // 1. 강의 기본 정보 조회
        LectureDTO lecture = lectureMapper.getLectureDetail(id);
        
        if (lecture != null) {
            // 2. 해당 강의의 섹션 목록 조회
            List<LectureDTO.SectionDTO> sections = lectureMapper.getSectionsByLectureId(id);
            
            if (sections != null && !sections.isEmpty()) {
                for (LectureDTO.SectionDTO section : sections) {
                    // 3. 각 섹션에 포함된 레슨 목록 조회 후 세팅
                    List<LectureDTO.LessonDTO> lessons = lectureMapper.getLessonsBySectionId(section.getSectionId());
                    section.setLessons(lessons);
                }
                // 4. 조립된 섹션 리스트를 강의 DTO에 주입
                lecture.setSections(sections);
            }
        }
        
        return lecture;
    }
    
    // 강의 생성
    @Transactional
    public Long createLecture(LectureCreateDTO createDTO) {
        Integer lgTypeId = convertLanguageToId(createDTO.getLanguage());
        
        Map<String, Object> lectureParams = new HashMap<>();
        lectureParams.put("mbId", createDTO.getMbId());
        lectureParams.put("title", createDTO.getTitle());
        lectureParams.put("content", createDTO.getDescription());
        lectureParams.put("lgTypeId", lgTypeId);
        lectureParams.put("cost", createDTO.getPrice());
        lectureParams.put("difficulty", createDTO.getLevel());
        
        lectureMapper.insertLecture(lectureParams);
        
        Long lectureId = ((Number) lectureParams.get("leId")).longValue();
        
        if (createDTO.getSections() != null) {
            int sectionOrder = 1;
            for (LectureCreateDTO.SectionDTO section : createDTO.getSections()) {
                Map<String, Object> sectionParams = new HashMap<>();
                sectionParams.put("leId", lectureId);
                sectionParams.put("title", section.getTitle());
                sectionParams.put("orderNum", sectionOrder);
                
                lectureMapper.insertSection(sectionParams);
                sectionOrder++;
                
                Long sectionId = ((Number) sectionParams.get("sectionId")).longValue();
                
                int lessonOrder = 1;
                for (LectureCreateDTO.LessonDTO lesson : section.getLessons()) {
                    lectureMapper.insertLesson(
                        sectionId,
                        lesson.getTitle(),
                        Integer.parseInt(lesson.getDuration()),
                        lessonOrder
                    );
                    lessonOrder++;
                }
            }
        }
        
        if (createDTO.getGeneratedSlots() != null) {
            for (String slot : createDTO.getGeneratedSlots()) {
                String[] parts = slot.split(" ");
                String dateStr = convertToDate(parts[0], parts[1]);
                String timeStr = parts[2];
                
                lectureMapper.insertSchedule(
                    lectureId,
                    dateStr,
                    timeStr,
                    createDTO.getEndTime(),
                    createDTO.getMaxStudents()
                );
            }
        }
        
        return lectureId;
    }
    
    private Integer convertLanguageToId(String language) {
        switch(language) {
            case "en": return 1;
            case "jp": return 2;
            case "cn": return 3;
            default: return 1;
        }
    }
    
    private String convertToDate(String month, String day) {
        String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        int monthNum = 1;
        for (int i = 0; i < months.length; i++) {
            if (months[i].equals(month)) {
                monthNum = i + 1;
                break;
            }
        }
        int year = java.time.Year.now().getValue();
        return String.format("%d-%02d-%02d", year, monthNum, Integer.parseInt(day));
    }
}