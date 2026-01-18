package com.seesun.global.exception;

import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.seesun.dto.exception.ErrorResponseDTO;

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
	
	// 전역 오류 핸들러
	// ErrorCode에 추가하면 알아서 핸들링 됨.
	@ExceptionHandler(GlobalException.class)
	protected ResponseEntity<ErrorResponseDTO> handleGlobalException(GlobalException e) {
		ErrorCode errorCode = e.getErrorCode();

        ErrorResponseDTO response = ErrorResponseDTO.builder()
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(response);
	}
}
