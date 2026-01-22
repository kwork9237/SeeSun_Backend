package com.seesun.mapper.lecture;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Map;

@Mapper
public interface DashboardMapper {
    Map<String, Object> getRecentLecture(@Param("mb_id") Long mbId);
}
