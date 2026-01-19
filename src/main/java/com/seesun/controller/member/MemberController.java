package com.seesun.controller.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seesun.dto.member.request.LoginRequestDTO;
import com.seesun.dto.member.request.MemberJoinDTO;
import com.seesun.service.member.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
	private final MemberService memberService;
	
	// 회원가입
	@PostMapping("/join")
	public ResponseEntity<?> signUp(@RequestBody MemberJoinDTO data) {
		memberService.insertMember(data);
		
		return ResponseEntity.ok("회원가입 성공");
	}
	
	// 중복 검사
	@PostMapping("/check")
	public ResponseEntity<?> check(@RequestParam(name="field") String f, @RequestParam(name="value") String v) {
		boolean res = memberService.checkDuplicate(f, v);
		
		return ResponseEntity.ok(res);
	}
	
	// 로그인
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDTO data) {
		String str = memberService.loginRequest(data);
		
		return ResponseEntity.ok(str);
	}
}
