package com.seesun.controller.lecture.session;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seesun.service.lecture.session.LectureSessionPoolSerivce;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lectures/sessions/pool")
public class LectureSessionPoolController {
//	private final LectureSessionPoolSerivce sessionPoolService;
	
//	public ResponseEntity<?> getPoolData() {
//		return ResponseEntity.ok(
//				sessionPoolService.getPoolData()
//			);
//	}
//	
//	public ResponseEntity<?> setActivateSessionPool() {
//		// roomId 세팅 필요
//		sessionPoolService.activateSessionByRoomId(null);
//		
//		return ResponseEntity.ok(null);
//	}
//	
//	public ResponseEntity<?> setDectivateSessionPool() {
//		// roomId 세팅 필요
//		sessionPoolService.deactivateSessionByRoomId(null);
//		
//		return ResponseEntity.ok(null);
//	}
}
