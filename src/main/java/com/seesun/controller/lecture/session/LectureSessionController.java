package com.seesun.controller.lecture.session;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seesun.security.userdetail.CustomUserDetails;
import com.seesun.service.lecture.session.LectureSessionService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lectures/sessions")
public class LectureSessionController {
	private final LectureSessionService sessionService;
	
	public record SessionRequestByLeId(Long leId) {}
	
	// 강의 세션 생성
	// 강의 시작 버튼 클릭 > 컨트롤러 실행 > 페이지 이동
	// front의 페이지 이동은 이 컨트롤러에서 return 된 uuid 값을 ID 삼아서 이동한다.
	// webRTC의 방 번호는 같이 return된 roomUuid를 활용한다. 
	@PostMapping("/create")
	public ResponseEntity<?> createSession(@AuthenticationPrincipal CustomUserDetails user, 
			@RequestBody SessionRequestByLeId req) {

		return ResponseEntity.ok(
				sessionService.create(user.getMbId(), req.leId())
			);
	}
	
	// 강의 세션 삭제 (종료)
	// 강의 진행 페이지에서 강의 중지 버튼 클릭 > 컨트롤러 실행 > 전체 종료
	@PostMapping("/close/{id}")
	public ResponseEntity<?> closeSession(@AuthenticationPrincipal CustomUserDetails user,
			@PathVariable("id") String uuid) {

		sessionService.close(user.getMbId(), uuid);
		
		return ResponseEntity.ok("성공");
	}
	
	// 강의 세션 접속
	// 멘티가 강의 접속 페이지 이동 >  
	@GetMapping("/room/{id}")
	public ResponseEntity<?> joinSession(@AuthenticationPrincipal CustomUserDetails user,
			@PathVariable("id") String uuid) {

		sessionService.validate(user.getMbId(), uuid);
		
		return ResponseEntity.ok("성공");
	}
	
	// 강의 시작
	@GetMapping("/start/{id}")
	public ResponseEntity<?> startSession(@AuthenticationPrincipal CustomUserDetails user,
			@PathVariable("id") String uuid) {
		
		sessionService.start(user.getMbId(), uuid);
		
		return ResponseEntity.ok(null);
	}
	
	// 세션 활성상태 체크
	@GetMapping("/check/{id}")
	public ResponseEntity<?> checkSession(@AuthenticationPrincipal CustomUserDetails user,
			@PathVariable("id") Long leId) {
		
		return ResponseEntity.ok(
				sessionService.check(leId)
			);
	}
}
