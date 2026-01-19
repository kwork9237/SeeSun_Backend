package com.seesun.service.mypage;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.seesun.dto.mypage.request.MyPageUpdateDTO;
import com.seesun.dto.mypage.request.PasswordUpdateDTO;
import com.seesun.global.exception.ErrorCode;
import com.seesun.global.exception.GlobalException;
import com.seesun.mapper.member.MemberMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyPageService {
	private final PasswordEncoder pwEncoder;
	private final MemberMapper memberMapper;
	
	// 회원탈퇴
	public void deleteMember(Long mbId, String password) {
		checkPassword(mbId, password);
		memberMapper.deleteMemberByMbId(mbId);
	}
	
	// 회원정보 수정
	public void updateMemberData(Long mbId, MyPageUpdateDTO data) {
		checkPassword(mbId, data.getPassword());
		
		try {
			memberMapper.updateMemberInfoByMbId(mbId, data.getMyPageData());
		} catch(DuplicateKeyException e) {
			throw new GlobalException(ErrorCode.DUPLICATE_MEMBER_DATA);
		}
	}
	
	// 비밀번호 수정
	public void updateMemberPassword(Long mbId, PasswordUpdateDTO data) {
		checkPassword(mbId, data.getOldPassword());
		memberMapper.updatePasswordByMbId(mbId, pwEncoder.encode(data.getNewPassword()));
	}
	
	// 비밀번호 검증
	private void checkPassword(Long mbId, String password) {
		// 오류날 경우 return null이 됨.
		String encodedPw = memberMapper.getPasswordByMbId(mbId);
		
		// null이면 회원정보가 없다는 뜻
		if(encodedPw == null)
			throw new GlobalException(ErrorCode.INCORRECT_MEMBER_DATA);

		// 비밀번호 불일치
	    if (!pwEncoder.matches(password, encodedPw))
	        throw new GlobalException(ErrorCode.PASSWORD_NOT_MATCH);
	}
}
