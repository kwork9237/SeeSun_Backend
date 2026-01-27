package com.seesun.dto.lecture.language;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("languageCategory")
public class LanguageCategoryDTO {
	private short lg_type_id;	// 언어 ID (AI 값)
	private String name;		// 언여명
	private String code;		// 언어 코드
}
