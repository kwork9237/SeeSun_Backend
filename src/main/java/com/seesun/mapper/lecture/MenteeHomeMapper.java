package com.seesun.mapper.lecture;

import com.seesun.dto.lecture.LectureDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface MenteeHomeMapper {
    List<LectureDTO> selectMenteeSchedules(Long mbId);
    List<LectureDTO> selectMenteeLectures(Long mbId);
}