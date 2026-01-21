package com.seesun.service.member;

import java.util.Set;

import com.seesun.dto.member.MyPageDTO;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.seesun.dto.member.request.LoginRequestDTO;
import com.seesun.dto.member.request.MemberJoinDTO;
import com.seesun.dto.member.request.MyPageUpdateDTO;
import com.seesun.dto.member.request.PasswordUpdateDTO;
import com.seesun.global.exception.ErrorCode;
import com.seesun.global.exception.GlobalException;
import com.seesun.mapper.member.MemberMapper;
import com.seesun.security.jwt.JwtTokenProvider;
import com.seesun.security.userdetail.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final AuthenticationManager authManager;
	private final PasswordEncoder pwEncoder;
	private final JwtTokenProvider jwtProvider;
	private final MemberMapper memberMapper;

	
	// 회원가입
	public void insertMember(MemberJoinDTO data) {
		try {
			// 비밀번호 암호화
			data.setPassword(pwEncoder.encode(data.getPassword()));
			
			// 회원 데이터 저장
			memberMapper.insertMember(data);
		} catch(Exception e) {
			throw new GlobalException(ErrorCode.UNKNOWN);
		}
	}
	
	// 중복 검사 (회원가입에 사용)
	public boolean checkDuplicate(String field, String value) {
		// 3개만 검사
	    if (!Set.of("username","nickname","phone").contains(field)) {
	        throw new GlobalException(ErrorCode.INVALID_REQUEST);
	    }
	    
	    return memberMapper.checkDuplicate(field, value);
	}
	
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
	
	// 로그인 요청
	public String loginRequest(LoginRequestDTO data) {
		String token;
		
		try {
			Authentication auth = authManager.authenticate(
					new UsernamePasswordAuthenticationToken(data.getUsername(), data.getPassword())
				);
	
			// auth 개체를 userdetail로 변환
			CustomUserDetails d = (CustomUserDetails) auth.getPrincipal();
	
			// 토큰 생성 및 반환 
			token = jwtProvider.createToken(d.getUsername(), d.getMbId());
			
			return token;
		} catch (AuthenticationException e) {
			throw new GlobalException(ErrorCode.IDPW_NOT_MATCH);
		}
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

	// 내 정보 조회
	public MyPageDTO getMemberInfo(Long mbId) {
		MyPageDTO info = memberMapper.getMyPageInfo(mbId);

		// 데이터가 없으면 예외 처리
		if(info == null) {
			throw new GlobalException(ErrorCode.INCORRECT_MEMBER_DATA);
		}
		return info;
	}
}
