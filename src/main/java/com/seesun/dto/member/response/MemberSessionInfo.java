package com.seesun.dto.member.response;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

// 실시간 강의에 필요한 회원정보
@Getter
@Setter
@Alias("memberSessionInfo")
public class MemberSessionInfo {
	private String nickname;
	private short mb_type_id;
}
