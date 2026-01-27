package com.seesun.service.lecture;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seesun.mapper.lecture.LectureMapper;
import com.seesun.mapper.lecture.language.LanguageCategoryMapper;
import lombok.RequiredArgsConstructor;

import com.seesun.dto.lecture.LectureDTO;
import com.seesun.dto.lecture.LectureCreateDTO;
import com.seesun.dto.lecture.SectionDTO; // 분리된 DTO 임포트
import com.seesun.dto.lecture.LessonDTO;  // 분리된 DTO 임포트

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LectureService {

    private final LectureMapper lectureMapper;
    private final LanguageCategoryMapper languageCategoryMapper;

    // 1. 강의 목록 조회
    public List<LectureDTO> getLectureList(String language, Integer difficulty, String tags, String timeSlot, String sortBy, String search) {
        String finalSortBy = (sortBy == null || sortBy.isEmpty()) ? "rating" : sortBy;
        return lectureMapper.getLectureList(language, difficulty, tags, timeSlot, finalSortBy, search);
    }
    
    // 2. 상세 조회
    public LectureDTO getLectureDetail(Long id) {
        LectureDTO lecture = lectureMapper.getLectureDetail(id);
        if (lecture != null) {
            // 외부 파일로 분리된 SectionDTO를 사용
            List<SectionDTO> sections = lectureMapper.getSectionsByLectureId(id);
            if (sections != null) {
                for (SectionDTO section : sections) {
                    section.setLessons(lectureMapper.getLessonsBySectionId(section.getSectionId()));
                }
                lecture.setSections(sections);
            }
        }
        return lecture;
    }

    // 3. 강의 생성
    @Transactional
    public Long createLecture(LectureCreateDTO createDTO, Long mbId) {
        try {
            createDTO.setMbId(mbId);
            short lgTypeId = languageCategoryMapper.getCategoryDataByCode(
        			createDTO.getLanguage().toUpperCase()).getLg_type_id();

            // MyBatis에서 le_id를 자동으로 DTO에 넣어줌
            lectureMapper.insertLecture(createDTO, lgTypeId);
            Long leId = createDTO.getLeId();

            if (createDTO.getSections() != null) {
                saveCurriculum(leId, createDTO.getSections());
            }

            if (createDTO.getGeneratedSlots() != null) {
                saveSchedules(leId, createDTO);
            }

            return leId;
        } catch (Exception e) {
            log.error("강의 생성 중 오류 발생: ", e);
            throw new RuntimeException("강의 저장 실패: " + e.getMessage());
        }
    }

    private void saveCurriculum(Long leId, List<SectionDTO> sections) {
        int sectionOrder = 1;
        for (SectionDTO section : sections) {
            lectureMapper.insertSection(leId, section, sectionOrder++);
            
            int lessonOrder = 1;
            for (LessonDTO lesson : section.getLessons()) {
                lectureMapper.insertLesson(
                    section.getSectionId(),
                    lesson.getTitle(),
                    Integer.parseInt(lesson.getDuration()),
                    lessonOrder++
                );
            }
        }
    }

    private void saveSchedules(Long leId, LectureCreateDTO createDTO) {
        for (String slot : createDTO.getGeneratedSlots()) {
            String[] parts = slot.split(" "); // "Jan 27 14:00" 형태
            if(parts.length < 3) continue;
            String dateStr = convertToDate(parts[0], parts[1]);
            lectureMapper.insertSchedule(leId, dateStr, parts[2], createDTO.getEndTime(), createDTO.getMaxStudents());
        }
    }

    private String convertToDate(String month, String day) {
        String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        int monthNum = 1;
        for (int i = 0; i < months.length; i++) {
            if (months[i].equals(month)) { monthNum = i + 1; break; }
        }
        return String.format("%d-%02d-%02d", java.time.Year.now().getValue(), monthNum, Integer.parseInt(day));
    }
}