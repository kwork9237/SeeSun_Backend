package com.seesun.service.member;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.seesun.dto.member.LoginRequestDTO;
import com.seesun.dto.member.MemberJoinDTO;
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
	
	// 로그인 요청
	public String loginRequest(LoginRequestDTO data) {
		Authentication auth = authManager.authenticate(
				new UsernamePasswordAuthenticationToken(data.getUsername(), data.getPassword())
			);

		// auth 개체를 userdetail로 변환
		CustomUserDetails d = (CustomUserDetails) auth.getPrincipal();

		// 토큰 생성 및 반환 
		String token = jwtProvider.createToken(d.getUsername(), d.getMbId());
		
		return token;
	}
	
	// 회원가입
	public void insertMember(MemberJoinDTO data) {
		try {
			data.setPassword(pwEncoder.encode(data.getPassword()));
			memberMapper.insertMember(data);
		} catch(Exception e) {
			// NullPointer 보내면 알아서 전역 핸들러가 500으로 반환
			throw new NullPointerException("회원가입 실패"); 
		}
	}
}
