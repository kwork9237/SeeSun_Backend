package com.seesun.mapper.file;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.seesun.dto.file.FileTypeDTO;

@Mapper
public interface FileTypeMapper {
	// 파일 업로드 타입 조회
	public FileTypeDTO getFileTypeByCode(@Param("code") String typeCode);
}
