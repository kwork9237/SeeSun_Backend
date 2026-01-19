package com.seesun.dto.member.request;

import lombok.Getter;
import lombok.Setter;

// 회원탈퇴 패스워드 인증
// Body String 대신 DTO를 통해 직렬화 일관성 유지
@Getter
@Setter
public class LeaveRequestDTO {
	private String password;
}
