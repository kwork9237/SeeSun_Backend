package com.seesun.dto.member;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

// 인증 시에만 사용
@Getter
@Setter
@Alias("authentication")
public class AuthenticationDTO {
	private Long mb_id;
	private String username;
	private String password;
	private short mb_type_id;
}
