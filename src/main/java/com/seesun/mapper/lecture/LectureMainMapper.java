package com.seesun.mapper.lecture;

import com.seesun.dto.lecture.MainLectureResponseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface LectureMainMapper {
    List<Map<String, Object>> selectLectureStats();
    List<MainLectureResponseDTO> selectPopularLectures(@Param("lg_type_id") Long lg_type_id);
}