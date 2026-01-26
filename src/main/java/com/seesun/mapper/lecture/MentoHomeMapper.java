package com.seesun.mapper.lecture;

import com.seesun.dto.lecture.LectureDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface MentoHomeMapper {
    // 1. 캘린더용 스케줄 조회
    List<LectureDTO> selectMentoSchedules(Long mbId);

    // 2. 하단 강의 목록 조회
    List<LectureDTO> selectMentoLectures(Long mbId);
}
