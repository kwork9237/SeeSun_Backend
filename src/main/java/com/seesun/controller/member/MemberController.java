package com.seesun.controller.member;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seesun.dto.member.request.LeaveRequestDTO;
import com.seesun.dto.member.request.LoginRequestDTO;
import com.seesun.dto.member.request.MemberJoinDTO;
import com.seesun.dto.member.response.LoginResponseDTO;
import com.seesun.security.userdetail.CustomUserDetails;
import com.seesun.service.member.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {
	private final MemberService memberService;
	
	// 회원가입
	@PostMapping("/join")
	public ResponseEntity<?> signUp(@RequestBody MemberJoinDTO data) {
		memberService.insertMember(data);
		
		return ResponseEntity.ok("회원가입 성공");
	}
	
	// 회원탈퇴
	// 현재 사용자를 의미하는 관례 상, 도메인주소를 me를 이용한다.
	// 단일 항목 dto를 사용하는 경우는 request의 일관성 유지를 위해서 사용한다.
	@DeleteMapping("/me")
	public ResponseEntity<?> leave(@AuthenticationPrincipal CustomUserDetails user, @RequestBody LeaveRequestDTO data) {
		memberService.deleteMember(user.getMbId(), data.getPassword());

		return ResponseEntity.ok("회원탈퇴 완료");
	}
	
	// 중복 검사
	// 유저 이메일, 닉네임, 핸드폰
	@GetMapping("/exists")
	public ResponseEntity<?> check(@RequestParam(name="field") String f, @RequestParam(name="value") String v) {
		boolean res = memberService.checkDuplicate(f, v);
		
		return ResponseEntity.ok(res);
	}
	
	// 로그인
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDTO data) {
		LoginResponseDTO res = memberService.loginRequest(data);
		
		return ResponseEntity.ok(res);
	}
}
