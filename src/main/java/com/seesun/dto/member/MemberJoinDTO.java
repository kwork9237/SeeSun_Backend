package com.seesun.dto.member;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

// 회원가입시에 사용
@Getter
@Setter
@Alias("memberJoin")
public class MemberJoinDTO {
	private String username;
	private String password;
	private String name;
	private String nickname;
	private String phone;
}
