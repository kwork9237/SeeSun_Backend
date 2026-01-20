package com.seesun.service.lecture;

import java.util.List;
import org.springframework.stereotype.Service;
import com.seesun.mapper.lecture.LectureMapper;
import dto.lecture.LectureDTO;
import lombok.RequiredArgsConstructor;

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
}