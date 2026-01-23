package com.seesun.controller.member;

import com.seesun.dto.mypage.MyPageDTO;
import com.seesun.dto.mypage.request.MyPageUpdateDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.seesun.dto.member.request.LoginRequestDTO;
import com.seesun.dto.member.request.MemberJoinDTO;

import com.seesun.dto.mypage.request.PasswordUpdateDTO;
import com.seesun.security.userdetail.CustomUserDetails;
import com.seesun.service.member.MemberService;

import lombok.RequiredArgsConstructor;

import java.util.Map;

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

	// [임시 회원탈퇴]
	// React에서 { data: { password: "..." } } 로 보내면 @RequestBody 로 받는 게 안전.
	// ======================================================================================================
	// MemberController.java

	@DeleteMapping("/leave/{mbId}")
	public ResponseEntity<?> leave(@PathVariable Long mbId, @RequestBody Map<String, Object> json) {

		String password = (String) json.get("password");

		memberService.deleteMember(mbId, password);
		return ResponseEntity.ok("회원탈퇴 완료");
	}
	// ======================================================================================================
	
	// 회원정보 수정
	@PostMapping("/update")
	public ResponseEntity<?> update(@AuthenticationPrincipal CustomUserDetails user, @RequestBody MyPageUpdateDTO data) {
		memberService.updateMemberData(user.getMbId(), data);
		
		return ResponseEntity.ok("회원정보 수정 완료");
	}

	// [임시 회원정보 수정]
	// ===========================================================================================
	@PostMapping("/update/{mbId}")
	public ResponseEntity<?> update(@PathVariable Long mbId, @RequestBody MyPageUpdateDTO data) {
		memberService.updateMemberData(mbId, data);
		return ResponseEntity.ok("회원정보 수정 완료");
	}
	// ===========================================================================================
	
	// 비밀번호 수정
	@PostMapping("/updatePassword")
	public ResponseEntity<?> updatePassword(@AuthenticationPrincipal CustomUserDetails user, @RequestBody PasswordUpdateDTO data) {
		memberService.updateMemberPassword(user.getMbId(), data);
		
		return ResponseEntity.ok("비밀번호 수정 완료");
	}

	// [임시 비밀번호 수정]
	// ===========================================================================================
	@PostMapping("/updatePassword/{mbId}")
	public ResponseEntity<?> updatePassword(@PathVariable Long mbId, @RequestBody PasswordUpdateDTO data) {
		memberService.updateMemberPassword(mbId, data);
		return ResponseEntity.ok("비밀번호 수정 완료");
	}
	// ===========================================================================================

	// 로그인
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDTO data) {
		String str = memberService.loginRequest(data);
		
		return ResponseEntity.ok(str);
	}

	//회원 정보 조회
	@GetMapping("/profile/{mbId}")
	public ResponseEntity<MyPageDTO> getProfile(@PathVariable Long mbId) {
		MyPageDTO myPage = memberService.getMyPageInfo(mbId);
		return ResponseEntity.ok(myPage);
	}

	/* [미래용: 토큰 버전] ------------------------------------------------
    @GetMapping("/profile")
    public ResponseEntity<MyPageDTO> getProfile(@AuthenticationPrincipal CustomUserDetails user) {
        Long mbId = user.getMbId();
        MyPageDTO myPage = myPageService.getMyPageInfo(mbId);
        return ResponseEntity.ok(myPage);
    }
    -------------------------------------------------------------------- */
}
