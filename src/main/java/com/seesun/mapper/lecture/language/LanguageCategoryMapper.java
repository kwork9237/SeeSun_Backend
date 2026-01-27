package com.seesun.mapper.lecture.language;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.seesun.dto.lecture.language.LanguageCategoryDTO;

@Mapper
public interface LanguageCategoryMapper {
	public LanguageCategoryDTO getCategoryDataByCode(@Param("code") String code);
}
