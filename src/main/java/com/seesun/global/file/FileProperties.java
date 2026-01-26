package com.seesun.global.file;

import java.nio.file.Path;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "file")
@Validated
public class FileProperties {
	// 설정에서 파일 정보를 가져오기 위해 사용
	
	@NotNull
	private Path basePath;
	
	@NotNull
	private List<String> allowedTypes;
}
