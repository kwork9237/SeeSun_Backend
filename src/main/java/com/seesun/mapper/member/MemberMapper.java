package com.seesun.mapper.member;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.seesun.dto.auth.AuthenticationDTO;
import com.seesun.dto.member.request.MemberJoinDTO;
import com.seesun.dto.member.request.MentoRequestDTO;
import com.seesun.dto.mypage.MyPageDTO;

@Mapper
public interface MemberMapper {
	// username를 받아서 내부 인증 개체 반환
	// 인증할때만 사용한다.
	public AuthenticationDTO getAuthenticationByUsername(@Param("username") String username);
	
	// 회원가입
	public void insertMember(MemberJoinDTO data);
	
	// 중복체크
	public int checkDuplicate(@Param("field") String field, @Param("value") String value);
	
	// 회원탈퇴
	public void deleteMemberByMbId(@Param("mb_id") Long mbId);	
	
	// 회원정보 수정
	public void updateMemberInfoByMbId(@Param("mb_id") Long mbId, @Param("dto") MyPageDTO data);
	
	// 비밀번호 수정
	public void updatePasswordByMbId(@Param("mb_id") Long mbId, @Param("password") String password);
	
	// 비밀번호 조회
	public String getPasswordByMbId(@Param("mb_id") Long mbId);

	// 관리자 확인
	public int checkAdmin(@Param("mb_id") Long mbId);
	
	// 멘토 요청 추가
	public void insertMentoRequest(MentoRequestDTO data);

	// 2026 01 27 수정필요
	// 내 정보 조회 (마이페이지용)
//	public MyPageDTO getMyPageInfo(@Param("mb_id") Long mbId);
}
