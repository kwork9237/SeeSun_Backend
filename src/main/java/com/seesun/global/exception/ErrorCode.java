package com.seesun.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
	// 관련 오류 목록
	// 가장 마지막 항목은 세미콜론 필수
	
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
	DUPLICATE_MEMBER_DATA(409, "M004", "중복되는 정보가 있습니다."),
	
	// 강의 관련 오류
	LECTURE_NOT_FOUND(500, "L001", "강의 정보가 존재하지 않습니다."),
	
	// 결제 관련 오류
	PAYMENT_FAIL(500, "P001", "결제 승인 실패"),
	PAYMENT_FULL(500,"P002", "수강 정원이 초과"),

	// WebRTC + Janus 관련 오류 (작성자:홍진기 - Janus 기반 리팩토링)
	WEBRTC_INVALID_SESSION(400, "W001", "유효하지 않은 세션 ID입니다."),
	WEBRTC_SESSION_ENDED(410, "W002", "이미 종료된 세션입니다."),

	WEBRTC_ROOM_NOT_FOUND(404, "W003", "존재하지 않는 Janus 방입니다."),
	WEBRTC_JANUS_COMM_ERROR(502, "W004", "Janus 서버와 통신 중 오류가 발생했습니다."),
	WEBRTC_JANUS_PLUGIN_ERROR(500, "W005", "Janus 플러그인 처리 중 오류가 발생했습니다."),

	WEBRTC_NOT_AUTHORIZED(403, "W006", "해당 WebRTC 기능에 접근할 권한이 없습니다."),
	WEBRTC_MENTOR_ONLY(403, "W007", "멘토만 수행할 수 있는 작업입니다."),
	WEBRTC_MENTEE_NOT_ENROLLED(403, "W008", "수강하지 않은 멘티는 입장할 수 없습니다."),

	WEBRTC_RECORDING_NOT_READY(202, "W009", "녹화 파일이 아직 준비되지 않았습니다."),
	WEBRTC_RECORDING_FAILED(500, "W010", "녹화 처리 중 오류가 발생했습니다."),
	WEBRTC_RECORDING_NOT_FOUND(404, "W011", "녹화 파일을 찾을 수 없습니다.");

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