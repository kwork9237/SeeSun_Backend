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

    public List<LectureDTO> getLectureList(
            String language, 
            Integer difficulty, 
            String tags, 
            String timeSlot, 
            String sortBy, 
            String search) {
        
        // sortBy 기본값 설정
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "rating";
        }
        
        return lectureMapper.getLectureList(language, difficulty, tags, timeSlot, sortBy, search);
    }
    
    public LectureDTO getLectureDetail(Long id) {
        return lectureMapper.getLectureDetail(id);
    }
    
    @Transactional
    public Long createLecture(LectureCreateDTO createDTO) {
        // 1. language 문자열을 lg_type_id로 변환
        Integer lgTypeId = convertLanguageToId(createDTO.getLanguage());
        
        // 2. lecture 테이블에 기본 정보 저장 (Map 사용)
        Map<String, Object> lectureParams = new HashMap<>();
        lectureParams.put("mbId", createDTO.getMbId());
        lectureParams.put("title", createDTO.getTitle());
        lectureParams.put("content", createDTO.getDescription());
        lectureParams.put("lgTypeId", lgTypeId);
        lectureParams.put("cost", createDTO.getPrice());
        lectureParams.put("difficulty", createDTO.getLevel());
        
        lectureMapper.insertLecture(lectureParams);
        
        // useGeneratedKeys로 자동 생성된 ID 가져오기
        Long lectureId = ((Number) lectureParams.get("leId")).longValue();
        
        // 3. 커리큘럼 저장 (sections & lessons)
        if (createDTO.getSections() != null) {
            int sectionOrder = 1;
            for (LectureCreateDTO.SectionDTO section : createDTO.getSections()) {
                Map<String, Object> sectionParams = new HashMap<>();
                sectionParams.put("leId", lectureId);
                sectionParams.put("title", section.getTitle());
                sectionParams.put("orderNum", sectionOrder);
                
                lectureMapper.insertSection(sectionParams);
                sectionOrder++;
                
                // useGeneratedKeys로 자동 생성된 section ID 가져오기
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
        
        // 4. 스케줄 저장
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
    
    // 언어 문자열을 ID로 변환
    private Integer convertLanguageToId(String language) {
        switch(language) {
            case "en": return 1; // 영어
            case "jp": return 2; // 일본어
            case "cn": return 3; // 중국어
            default: return 1;
        }
    }
    
    // 날짜 문자열 변환 (Jan 20 -> 2024-01-20)
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