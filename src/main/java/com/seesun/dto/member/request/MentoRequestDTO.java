package com.seesun.dto.member.request;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("mentoRequest")
public class MentoRequestDTO {
	private Long mb_id;
	private String details;
	private Long file_id;
}
