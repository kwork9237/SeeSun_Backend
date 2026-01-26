package com.seesun.service.file;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.seesun.dto.file.FileDataDTO;
import com.seesun.dto.file.FileSaveResult;
import com.seesun.global.exception.ErrorCode;
import com.seesun.global.exception.GlobalException;
import com.seesun.global.file.FileManager;
import com.seesun.mapper.file.FileMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FileService {
	private final FileManager fileManager;
	private final FileMapper fileMapper;
	
	@Transactional
	public void save(Long mbId, MultipartFile file) {
		// 파일 저장
		FileSaveResult fs = fileManager.save(file);
		
		try {
			// DB에 저장된 파일정보 삽입
			FileDataDTO fd = FileDataDTO.from(fs)
					.mb_id(mbId)
					.build();
			
			fileMapper.insertFileData(fd);
		} catch(Exception e) {
			// DB 입력 실패시 파일데이터 제거 및 오류 내용 출력
			fileManager.delete(fs.relativePath());
			
			e.printStackTrace();
			throw new GlobalException(ErrorCode.DATABASE_INSERT_ERROR);
		}
	}
	
	@Transactional
	public void delete(String path) {
		
		// 파일이 정상제거 되지 않을 경우
		if(!fileManager.delete(path)) {
			throw new GlobalException(ErrorCode.FILE_NOT_FOUND);
		}
		
		try {
			fileMapper.deleteFileData();
		} catch(Exception e) {
			e.printStackTrace();
			throw new GlobalException(ErrorCode.DATABASE_DELETE_ERROR);
		}
	}
	
	public void download() {
		
	}
}
