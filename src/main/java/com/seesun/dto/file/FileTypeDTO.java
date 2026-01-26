package com.seesun.dto.file;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("fileType")
public class FileTypeDTO {
	private short type_id;
	private String code;
}
