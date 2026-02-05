package com.seesun.dto.file.result;

import org.springframework.core.io.Resource;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileDownloadResult {
	private final Resource resource;
	private final String encodedName;
	private final String originalName;
	private final Long size;
	private final String contentType;
}
