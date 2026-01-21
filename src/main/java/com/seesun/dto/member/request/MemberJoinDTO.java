package com.seesun.dto.member.request;

import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

// 회원가입시에 사용
@Getter
@Setter
@Alias("memberJoin")
public class MemberJoinDTO {
	@JsonProperty("email")		// Front에서는 Email과 매핑됨
	private String username;	// ID
	private String password;	// PW
	private String name;		// Name
	private String nickname;	// 강의에서 보여질 이름
	private String phone;		// Phone
}
