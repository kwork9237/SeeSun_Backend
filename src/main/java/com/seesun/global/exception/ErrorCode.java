package com.seesun.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
	// 관련 오류 목록
	
	UNKNOWN(500, "U000", "알 수 없는 오류"),
	INVALID_REQUEST(401, "U001", "잘못된 입력"),
	UNAUTHORIZED(401, "U002", "로그인 상태에서만 가능합니다."),
	
	// 파일 관련 오류
	FILE_SAVE_FAIL(500, "F001", "파일 저장 실패"),
	FILE_DELETE_FAIL(500, "F002", "파일 삭제 실패"),
	DISALLOWED_CONTENT_TYPE(401, "F003", "허용되지 않은 파일 타입"),
	INCORRECT_FILE_TYPE(401, "F004", "올바르지 않은 파일 타입"),
	INVALID_FILE_PATH(500, "F005", "잘못된 경로 지정"),
	EMPTY_FILE_PATH(500, "F006", "빈 파일 경로"),
	FILE_EMPTY(401, "F007", "빈 파일 데이터"),
	FILE_NOT_FOUND(500, "F004", "존재하지 않는 파일"),
	LARGE_FILE_SIZE(401, "F008", "파일 업로드 한계 초과"),
	
	// DB 관련 오류
	DATABASE_INSERT_ERROR(500, "D001", "데이터 입력 실패"),
	DATABASE_DELETE_ERROR(500, "D002", "데이터 삭제 실패"),
	
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