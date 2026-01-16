package com.seesun.mapper.member;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.seesun.dto.member.AuthenticationDTO;
import com.seesun.dto.member.MemberJoinDTO;

@Mapper
public interface MemberMapper {
	// username를 받아서 내부 인증 개체 반환
	public AuthenticationDTO getAuthenticationByUsername(@Param("username") String username);
	
	// 회원가입
	public void insertMember(MemberJoinDTO data);
}
