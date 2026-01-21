package com.seesun.controller.mypage;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seesun.dto.admin.AdminDto;
import com.seesun.dto.mypage.request.MyPageUpdateDTO;
import com.seesun.dto.mypage.request.PasswordUpdateDTO;
import com.seesun.security.userdetail.CustomUserDetails;
import com.seesun.service.mypage.MyPageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MyPageController {
	private final MyPageService myPageService;

	// 회원정보 수정
	@PatchMapping("/profile")
	public ResponseEntity<?> update(@AuthenticationPrincipal CustomUserDetails user, @RequestBody MyPageUpdateDTO data) {
		myPageService.updateMemberData(user.getMbId(), data);
		
		return ResponseEntity.ok("회원정보 수정 완료");
	}
	
	// 비밀번호 수정
	@PatchMapping("/password")
	public ResponseEntity<?> updatePassword(@AuthenticationPrincipal CustomUserDetails user, @RequestBody PasswordUpdateDTO data) {
		myPageService.updateMemberPassword(user.getMbId(), data);
		
		return ResponseEntity.ok("비밀번호 수정 완료");
	}
	
	// 회원 타입 반환
	@GetMapping("/member-type")
	public ResponseEntity<?> getMemberTypeId(@AuthenticationPrincipal CustomUserDetails user) {
		return ResponseEntity.ok(user.getMbTypeId());
	}
	
	//관리자 메인
	@GetMapping("/dashboard-stats")
    public ResponseEntity<AdminDto> getDashboardStats() {
        AdminDto stats = myPageService.getDashboardStats();
        System.out.println("stats:"+ stats);
        
        return ResponseEntity.ok(stats);
    }
}
