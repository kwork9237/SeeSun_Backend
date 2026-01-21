package com.seesun.service.lecture;

import com.seesun.dto.lecture.MainLectureResponseDTO;
import java.util.List;
import java.util.Map;

public interface LectureMainService {
    Map<Long, Long> getLectureCounts();
    List<MainLectureResponseDTO> getPopularLectures(Long lgType);
}