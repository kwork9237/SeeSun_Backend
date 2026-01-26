package com.seesun.dto.file;

import org.apache.ibatis.type.Alias;

import com.seesun.dto.file.result.FileSaveResult;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Alias("fileData")
public class FileDataDTO {
	// 파일 정보 저장
	private Long mb_id;				// 회원 ID
	private String original_name;	// 원본 파일명
	private String stored_name;		// UUID 파일명
	private String relative_path;	// 상대경로
	private Long size;				// 파일 크기 (byte)
	private String ext;				// 확장자명
	private short type_id;			// 상황에 따라 지정필요

	// builder class
	// FileSaveResult 클래스를 넣고 build 하면 바로 변환된다.
	public static FileDataDTOBuilder from(FileSaveResult fs) {
		return FileDataDTO.builder()
				.original_name(fs.originalName())
				.stored_name(fs.storedName())
				.relative_path(fs.relativePath())
				.size(fs.size())
				.ext(fs.ext());
	}
}
