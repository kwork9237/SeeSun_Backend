package com.seesun.dto.member.request;

import lombok.Getter;
import lombok.Setter;

// 회원 로그인 요청
@Getter
@Setter
public class LoginRequestDTO {
	private String username;
	private String password;
}
