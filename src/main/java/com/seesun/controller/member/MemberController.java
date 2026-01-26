package com.seesun.controller.member;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.seesun.dto.member.request.EmailVerifyRequestDTO;
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
	
	// 회원가입 (멘티)
	@PostMapping("/join")
	public ResponseEntity<?> signUpMember(@RequestBody MemberJoinDTO data) {
	    memberService.insertMember(data, null);
	    return ResponseEntity.ok("회원가입 성공");
	}
	
	
	// 회원가입 (멘토)
	@PostMapping(value = "/join-mentor", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> signUp(
			@RequestPart("data")  MemberJoinDTO data, 
			@RequestPart(value = "file", required = false) MultipartFile file) {
		memberService.insertMember(data, file);
		
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
	public ResponseEntity<Boolean> check(@RequestParam(name="field") String f, @RequestParam(name="value") String v) {
		boolean res = memberService.checkDuplicate(f, v);
		
		return ResponseEntity.ok(res);
	}
	
	// 이메일 번호 인증
	@PostMapping("/email/verify-code")
	public ResponseEntity<Boolean> emailVerify(@RequestBody EmailVerifyRequestDTO data) {
		if(data.getCode().equals("123456")) return ResponseEntity.ok(true);
		
		return ResponseEntity.ok(false);
	}
	
	// 로그인
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDTO data) {
		LoginResponseDTO res = memberService.loginRequest(data);
		
		return ResponseEntity.ok(res);
	}
	

	//회원 정보 조회
//	@GetMapping("/profile/{mbId}")
//	public ResponseEntity<MyPageDTO> getProfile(@PathVariable Long mbId) {
//		MyPageDTO myPage = memberService.getMyPageInfo(mbId);
//		return ResponseEntity.ok(myPage);
//	}

	/* [미래용: 토큰 버전] ------------------------------------------------
    @GetMapping("/profile")
    public ResponseEntity<MyPageDTO> getProfile(@AuthenticationPrincipal CustomUserDetails user) {
        Long mbId = user.getMbId();
        MyPageDTO myPage = myPageService.getMyPageInfo(mbId);
        return ResponseEntity.ok(myPage);
    }
    -------------------------------------------------------------------- */
}
