package com.seesun.dto.file;

// 파일 저장 결과
public record FileSaveResult(
		String relativePath,
	    String storedName,
	    String originalName,
	    long size,
	    String ext
	) {
	
}
