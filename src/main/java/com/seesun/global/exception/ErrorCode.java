package com.seesun.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
	// 관련 오류 목록
	
	UNKNOWN(500, "U000", "알 수 없는 오류"),
	INVALID_REQUEST(401, "U001", "잘못된 입력"),
	
	// 회원 관련 오류
	IDPW_NOT_MATCH(401, "M001", "아이디 또는 비밀번호 오류"),
	PASSWORD_NOT_MATCH(401, "M002", "비밀번호가 일치하지 않습니다."),
	INCORRECT_MEMBER_DATA(401, "M003", "회원정보를 찾을 수 없습니다."),
	DUPLICATE_MEMBER_DATA(409, "M004", "중복되는 정보가 있습니다."),		// 마지막에 세미콜론 필요

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