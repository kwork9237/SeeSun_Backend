package com.seesun.dto.file.response;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("fileDownload")
public class FileDownloadDTO {
	private Long file_id;
	private Long mb_id;
	private String original_name;
	private String stored_name;
	private String relative_path;
	private Long size;
	private String ext;
}
