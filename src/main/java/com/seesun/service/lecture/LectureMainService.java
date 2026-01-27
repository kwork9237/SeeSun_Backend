package com.seesun.service.lecture;

import com.seesun.dto.lecture.MainLectureResponseDTO;
import com.seesun.mapper.lecture.LectureMainMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LectureMainService {

    private final LectureMainMapper lectureMainMapper;

    @Transactional(readOnly = true)
    public Map<Long, Long> getLectureCounts() {
        List<Map<String, Object>> resultList = lectureMainMapper.selectLectureStats();

        Map<Long, Long> stats = new HashMap<>();
        stats.put(1L, 0L); // 영어 기본값
        stats.put(2L, 0L); // 일어 기본값
        stats.put(3L, 0L); // 중국어 기본값

        for (Map<String, Object> row : resultList) {
            Long typeId = ((Number) row.get("lg_type_id")).longValue();
            Long count = ((Number) row.get("count")).longValue();
            stats.put(typeId, count);
        }
        
        return stats;
    }

    @Transactional(readOnly = true)
    public List<MainLectureResponseDTO> getPopularLectures(Long lgType) {
        return lectureMainMapper.selectPopularLectures(lgType);
    }
}