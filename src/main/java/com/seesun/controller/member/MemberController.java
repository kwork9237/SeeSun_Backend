package com.seesun.controller.member;

import com.seesun.dto.member.MyPageDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.seesun.dto.member.request.LoginRequestDTO;
import com.seesun.dto.member.request.MemberJoinDTO;
import com.seesun.dto.member.request.MyPageUpdateDTO;
import com.seesun.dto.member.request.PasswordUpdateDTO;
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
		memberService.updateMemberData(8L, data);
		
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
	
	// 정보 가져오기
	@GetMapping("/info")
	public ResponseEntity<?> getMemberInfo(@AuthenticationPrincipal CustomUserDetails user) {
		// 토큰(user)에서 mbId를 꺼내서 서비스를 호출
		MyPageDTO info = memberService.getMemberInfo(user.getMbId());

		return ResponseEntity.ok(info);
	}
}
