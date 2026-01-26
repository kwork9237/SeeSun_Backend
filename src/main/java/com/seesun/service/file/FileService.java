package com.seesun.service.file;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.seesun.dto.file.FileDataDTO;
import com.seesun.dto.file.FileTypeDTO;
import com.seesun.dto.file.response.FileDataResponseDTO;
import com.seesun.dto.file.response.FileDownloadDTO;
import com.seesun.dto.file.result.FileSaveResult;
import com.seesun.global.exception.ErrorCode;
import com.seesun.global.exception.GlobalException;
import com.seesun.global.file.FileManager;
import com.seesun.mapper.file.FileMapper;
import com.seesun.mapper.file.FileTypeMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FileService {
	private final FileManager fileManager;
	private final FileMapper fileMapper;
	private final FileTypeMapper fileTypeMapper;
	
	// 저장
	@Transactional
	public void save(Long mbId, String code, MultipartFile file) {
		// 파일 타입 검증
		FileTypeDTO ft = validate(code);
		
		// 파일 저장
		FileSaveResult fs = fileManager.save(file);
		
		try {
			// DB에 저장된 파일정보 삽입
			FileDataDTO fd = FileDataDTO.from(fs)
					.mb_id(mbId)
					.type_id(ft.getType_id())
					.build();

			fileMapper.insertFileData(fd);
		} catch(Exception e) {
			// DB 입력 실패시 파일데이터 제거 및 오류 내용 출력
			fileManager.delete(fs.relativePath());
			
			e.printStackTrace();
			throw new GlobalException(ErrorCode.DATABASE_INSERT_ERROR);
		}
	}
	
	// 삭제
	@Transactional
	public void delete(Long mbId, String code) {
		// 파일 타입 검증
		FileTypeDTO ft = validate(code);
		
		// 파일 경로 가져오기
		String path = fileMapper.getFilePath(mbId, ft.getType_id());
		
		try {
			// DB에서 파일 삭제
			fileMapper.deleteFileData(mbId, ft.getType_id(), path);
		} catch(Exception e) {
			e.printStackTrace();
			throw new GlobalException(ErrorCode.DATABASE_DELETE_ERROR);
		}
		
		// 파일이 정상제거 되지 않을 경우
		if(!fileManager.delete(path)) {
			throw new GlobalException(ErrorCode.FILE_DELETE_FAIL);
		}
	}
	
	// 파일 목록 조회
	public List<FileDataResponseDTO> list(short typeId) {
		return fileMapper.getFileListByType(typeId);
	}
	
	// 다운로드
	public FileDownloadDTO downloadData(Long fileId) {
        FileDownloadDTO file = fileMapper.getFileForDownload(fileId);

        if (file == null)
    		throw new GlobalException(ErrorCode.FILE_NOT_FOUND);
        
        return file;
    }
	
	// 파일 코드 검증
	private FileTypeDTO validate(String code) {
		// DB에서 파일코드 조회
		FileTypeDTO data = fileTypeMapper.getFileTypeByCode(code);
		
		// 없으면 잘못된 파일 타입으로 리턴
		if(data == null)
			throw new GlobalException(ErrorCode.INCORRECT_FILE_TYPE);
		
		// 정상일 경우 DTO 리턴
		return data;
	}
}
