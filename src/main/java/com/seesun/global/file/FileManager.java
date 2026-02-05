package com.seesun.global.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.seesun.dto.file.result.FileSaveResult;
import com.seesun.global.exception.ErrorCode;
import com.seesun.global.exception.GlobalException;
import com.seesun.global.uuid.UUIDUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileManager {
	// application 파일 설정값
	private final FileProperties props;
	
	// 파일 저장
	public FileSaveResult save(MultipartFile file) {
		// 파일 검증
		validate(file);

		try {
			LocalDate now = LocalDate.now();
			
			// 폴더 위치 지정
			Path dir = props.getBasePath()
					.resolve(String.valueOf(now.getYear()))
					.resolve(String.format("%02d", now.getMonthValue()))
					.resolve(String.format("%02d", now.getDayOfMonth()));
			
			// 폴더 생성 (있으면 통과)
			Files.createDirectories(dir);
			
			// 확장자, 원본파일명
			String original = file.getOriginalFilename() == null ? "file" : file.getOriginalFilename();
			String ext = extractExt(original).replace(".", "").toLowerCase();
			
			// UUID 이름
			String stored = UUIDUtil.generate() + "." + ext;
			
			// 실제 저장
			Path target = dir.resolve(stored);
			file.transferTo(target);
			
			// 상대경로
			Path relative = props.getBasePath().relativize(target);
			String relativePath = relative.toString().replace(File.separatorChar, '/');
			
			log.info("[FILEMANAGER] ILE SAVED PATH = {}", target.toAbsolutePath());
			
			// 결과 반환
			return new FileSaveResult(relativePath, stored, original, file.getSize(), ext);
			
		} catch (IOException e) {
			throw new GlobalException(ErrorCode.FILE_SAVE_FAIL);
		}
	}
	
	// 파일 삭제
	public boolean delete(String relativePath) {
		if (relativePath == null || relativePath.isBlank()) {
			throw new GlobalException(ErrorCode.EMPTY_FILE_PATH);
		}
		
		try {
			// basePath + relativePath 결합
			Path target = props.getBasePath().resolve(relativePath).normalize();
			
			 // 경로 탈출 방지 (../../windows 같은 공격 차단)
			if (!target.startsWith(props.getBasePath().normalize())) {
				throw new GlobalException(ErrorCode.INVALID_FILE_PATH);
			}
			
			// 존재하면 삭제, 없으면 false
			return Files.deleteIfExists(target);
		} catch(IOException e) {
			throw new GlobalException(ErrorCode.FILE_DELETE_FAIL);
		}
	}
	

	// 파일 검증 로직
	private void validate(MultipartFile file) {
		// 빈 파일
	    if (file == null || file.isEmpty()) 
	    	throw new GlobalException(ErrorCode.FILE_EMPTY);
	    
	    // 파일 확장자 오류
	    String ct = file.getContentType();
	    if (ct == null || !props.getAllowedTypes().contains(ct))
	    	throw new GlobalException(ErrorCode.DISALLOWED_CONTENT_TYPE);
	  }

	// 확장자 추출
	private static String extractExt(String name) {
		// . 붙은 곳의 위치
	    int idx = name.lastIndexOf('.');
	    
	    // 확장자가 없는 경우
	    if (idx < 0 || idx == name.length() - 1) return "";
	    
	    // 확장자를 소문자 변환하여 리턴
	    return name.substring(idx).toLowerCase();
	}
}
