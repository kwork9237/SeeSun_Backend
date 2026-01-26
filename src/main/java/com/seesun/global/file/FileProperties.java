package com.seesun.global.file;

import java.nio.file.Path;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.unit.DataSize;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "file")
@Validated
public class FileProperties {
	@NotNull
	private Path basePath;
	
	@NotNull
	private DataSize maxSize;
	
	@NotNull
	private List<String> allowedTypes;
}
