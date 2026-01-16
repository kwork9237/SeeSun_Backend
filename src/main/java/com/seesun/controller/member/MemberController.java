package com.seesun.controller.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seesun.dto.member.LoginRequestDTO;
import com.seesun.dto.member.MemberJoinDTO;
import com.seesun.service.member.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
	private final MemberService memberService;
	
	// 회원가입
	@PostMapping("/join")
	public ResponseEntity<?> signUp(@RequestBody MemberJoinDTO data) {
		memberService.insertMember(data);
		
		return ResponseEntity.ok().body("회원가입 성공");
	}
	
	// 로그인
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDTO data) {
		String str = memberService.loginRequest(data);
		
		return ResponseEntity.ok(str);
	}
}
