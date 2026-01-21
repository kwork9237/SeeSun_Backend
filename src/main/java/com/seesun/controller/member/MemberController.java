package com.seesun.controller.member;

import com.seesun.dto.mypage.request.MyPageUpdateDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seesun.dto.member.request.LoginRequestDTO;
import com.seesun.dto.member.request.MemberJoinDTO;

import com.seesun.dto.mypage.request.PasswordUpdateDTO;
import com.seesun.security.userdetail.CustomUserDetails;
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
	
	// 회원탈퇴
	// body 데이터는 json 형식이 아니라 plain text 형식이여야 한다.
	@DeleteMapping("/leave")
	public ResponseEntity<?> leave(@AuthenticationPrincipal CustomUserDetails user, @RequestBody String password) {
		memberService.deleteMember(user.getMbId(), password);

		return ResponseEntity.ok("회원탈퇴 완료");
	}
	
	// 회원정보 수정
	@PostMapping("/update")
	public ResponseEntity<?> update(@AuthenticationPrincipal CustomUserDetails user, @RequestBody MyPageUpdateDTO data) {
		memberService.updateMemberData(user.getMbId(), data);
		
		return ResponseEntity.ok("회원정보 수정 완료");
	}
	
	// 비밀번호 수정
	@PostMapping("/updatePassword")
	public ResponseEntity<?> updatePassword(@AuthenticationPrincipal CustomUserDetails user, @RequestBody PasswordUpdateDTO data) {
		memberService.updateMemberPassword(user.getMbId(), data);
		
		return ResponseEntity.ok("비밀번호 수정 완료");
	}
	
	// 로그인
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDTO data) {
		String str = memberService.loginRequest(data);
		
		return ResponseEntity.ok(str);
	}
}
