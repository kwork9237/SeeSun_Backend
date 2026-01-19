package com.seesun.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
	// 관련 오류 목록
	
	UNKNOWN(500, "U000", "알 수 없는 오류"),
	INVALID_REQUEST(401, "U001", "잘못된 입력"),
	UNAUTHORIZED(401, "U002", "로그인 상태에서만 가능합니다."),
	
	// 회원 관련 오류
	IDPW_NOT_MATCH(401, "M001", "아이디 또는 비밀번호 오류"),
	PASSWORD_NOT_MATCH(401, "M002", "비밀번호가 일치하지 않습니다."),
	INCORRECT_MEMBER_DATA(401, "M003", "회원정보를 찾을 수 없습니다."),
	DUPLICATE_MEMBER_DATA(409, "M004", "중복되는 정보가 있습니다.");		// 마지막에 세미콜론 필요
	
	
	// 실제 블럭 정의
	private final int status;		// HTTP 반환 코드
	private final String code;		// 사용자 정의 코드
	private final String message;	// 오류 메시지
	
	// 생성자
	// Enum은 상수 집합이므로 Setter 사용 금지
	ErrorCode(int status, String code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}
}