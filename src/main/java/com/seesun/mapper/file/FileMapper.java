package com.seesun.mapper.file;

import org.apache.ibatis.annotations.Mapper;

import com.seesun.dto.file.FileDataDTO;

@Mapper
public interface FileMapper {
	public void insertFileData(FileDataDTO fileData);
	public void deleteFileData();
}
