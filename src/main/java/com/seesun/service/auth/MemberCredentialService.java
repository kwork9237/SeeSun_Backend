package com.seesun.service.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.seesun.global.exception.ErrorCode;
import com.seesun.global.exception.GlobalException;
import com.seesun.mapper.member.MemberMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberCredentialService {
	// 현재는 비밀번호 인증에만 사용
	// 나중에 재인증 등에 사용 여지가 있음.	
	
	private final PasswordEncoder pwEncoder;
	private final MemberMapper memberMapper;
	
	// 비밀번호 검증
	public void checkPassword(Long mbId, String password) {
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
