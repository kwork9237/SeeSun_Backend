package com.seesun.config.file;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.seesun.global.file.FileProperties;

@Configuration
@EnableConfigurationProperties(FileProperties.class)
public class FileConfig {
	// 설정값을 정상적으로 가져오기 위한 클래스 파일.
}