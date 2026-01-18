package com.seesun.global.exception;

public class GlobalException extends RuntimeException {
	// 시리얼라이즈 경고 해결
	private static final long serialVersionUID = 1L;
	
	private final ErrorCode errorCode;
	
	public GlobalException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
