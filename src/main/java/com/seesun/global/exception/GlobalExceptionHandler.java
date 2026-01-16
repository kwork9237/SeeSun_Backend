package com.seesun.global.exception;

import org.apache.coyote.BadRequestException;
import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	/*
	 * 전역 예외 처리 핸들러
	 * Service에서는 throw new ---Exception
	 * 이쪽에서 해당 Exception 들 처리.
	 */
	
	// BadRequest의 예외처리
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<?> handleBadRequest(BadRequestException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}
	
	// NullPointer은 Inter Server 처리
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<?> handleNullPointer(NullPointerException e) {
		return ResponseEntity.internalServerError().body(e.getMessage());
	}
	
	// 로그인 예외 처리 (로그인시에만 호출됨)
	@ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleLoginFail(AuthenticationException e) {
		return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).body("ID 또는 PW가 다릅니다.");
    }
}
