package com.kcd.pos.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    SEQUENCE_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "E0001", "시퀀스 저장 중 오류가 발생했습니다.")
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
    public HttpStatus getStatus() { return status; }
    public String getCode() { return code; }
    public String getMessage() { return message; }
}
