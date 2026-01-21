package com.seesun.mapper.member;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.seesun.dto.auth.AuthenticationDTO;
import com.seesun.dto.member.request.MemberJoinDTO;
import com.seesun.dto.mypage.MyPageDTO;

@Mapper
public interface MemberMapper {
	// username를 받아서 내부 인증 개체 반환
	// 인증할때만 사용한다.
	public AuthenticationDTO getAuthenticationByUsername(@Param("username") String username);
	
	// 회원가입
	public void insertMember(MemberJoinDTO data);
	
	// 중복체크
	public boolean checkDuplicate(@Param("field") String field, @Param("value") String value);
	
	// 회원탈퇴
	public void deleteMemberByMbId(@Param("mb_id") Long mbId);	
	
	// 회원정보 수정
	public void updateMemberInfoByMbId(@Param("mb_id") Long mbId, @Param("dto") MyPageDTO data);
	
	// 비밀번호 수정
	public void updatePasswordByMbId(@Param("mb_id") Long mbId, @Param("password") String password);
	
	// 비밀번호 조회
	public String getPasswordByMbId(@Param("mb_id") Long mbId);
	
	// 관리자 메인
	// 3가지 통계를 한 번에 가져오거나, 각각 가져올 수 있습니다.
    // 여기서는 각각 조회하여 DTO를 조립하는 방식을 사용합니다.    
    int countNewMentorRequests();   	// 멘토 신청 카운트
    int countReportedLectures();    	// 신고 강의 카운트
//  int countUnansweredInquiries(); 	// 미답변 건의사항 카운트
	
}
