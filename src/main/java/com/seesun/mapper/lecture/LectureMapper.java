package com.seesun.mapper.lecture;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import dto.lecture.LectureDTO;

@Mapper
public interface LectureMapper {
    
    List<LectureDTO> getLectureList(
        @Param("language") String language,
        @Param("difficulty") Integer difficulty,
        @Param("tags") String tags,
        @Param("timeSlot") String timeSlot,
        @Param("sortBy") String sortBy,
        @Param("search") String search
    );
    
    LectureDTO getLectureDetail(@Param("id") Long id);
}