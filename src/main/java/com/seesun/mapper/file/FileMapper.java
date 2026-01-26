package com.seesun.mapper.file;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.seesun.dto.file.FileDataDTO;
import com.seesun.dto.file.response.FileDataResponseDTO;
import com.seesun.dto.file.response.FileDownloadDTO;

@Mapper
public interface FileMapper {
	// 파일정보 입력, 삭제
	public void insertFileData(FileDataDTO fileData);
	public void deleteFileData(@Param("mb_id") Long mbId, @Param("type_id") short typeId, @Param("relative_path") String path);
	
	// 파일 경로 가져오기
	public String getFilePath(@Param("mb_id") Long mbId, @Param("type_id") short typeId);
	
	// 파일 타입별 목록 가져오기 (시간순)
	public List<FileDataResponseDTO> getFileListByType(@Param("type_id") short typeId);
	
	// 다운로드용 파일 데이터 가져오기
	public FileDownloadDTO getFileForDownload(@Param("file_id") Long fileId);
}
