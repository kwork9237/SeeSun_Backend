package com.seesun.dto.file.response;

import java.time.LocalDateTime;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("fileDataResponse")
public class FileDataResponseDTO {
	private Long file_id;
	private String original_name;
	private Long size;
	private LocalDateTime created_at; 
}
