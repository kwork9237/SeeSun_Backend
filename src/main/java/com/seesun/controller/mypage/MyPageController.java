package com.seesun.controller.mypage;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seesun.dto.mypage.request.MyPageUpdateDTO;
import com.seesun.dto.mypage.request.PasswordUpdateDTO;
import com.seesun.security.userdetail.CustomUserDetails;
import com.seesun.service.mypage.MyPageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/myPage")
public class MyPageController {
	private final MyPageService myPageService;
	
	// 회원탈퇴
	// body 데이터는 json 형식이 아니라 plain text 형식이여야 한다.
	@DeleteMapping("/leave")
	public ResponseEntity<?> leave(@AuthenticationPrincipal CustomUserDetails user, @RequestBody String password) {
		myPageService.deleteMember(user.getMbId(), password);

		return ResponseEntity.ok("회원탈퇴 완료");
	}
	
	// 회원정보 수정
	@PatchMapping("/update")
	public ResponseEntity<?> update(@AuthenticationPrincipal CustomUserDetails user, @RequestBody MyPageUpdateDTO data) {
		myPageService.updateMemberData(user.getMbId(), data);
		
		return ResponseEntity.ok("회원정보 수정 완료");
	}
	
	// 비밀번호 수정
	@PatchMapping("/updatePassword")
	public ResponseEntity<?> updatePassword(@AuthenticationPrincipal CustomUserDetails user, @RequestBody PasswordUpdateDTO data) {
		myPageService.updateMemberPassword(user.getMbId(), data);
		
		return ResponseEntity.ok("비밀번호 수정 완료");
	}
}
