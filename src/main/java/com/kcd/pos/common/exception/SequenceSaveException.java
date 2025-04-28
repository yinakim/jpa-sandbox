package com.kcd.pos.common.exception;

public class SequenceSaveException extends RuntimeException{

    private final ErrorCode errorCode;
    private final String detail;

    public SequenceSaveException(ErrorCode errorCode, String detail) {
        super(errorCode.getMessage() + " (" + detail + ")");
        this.errorCode = errorCode;
        this.detail = detail;
    }

    public SequenceSaveException(ErrorCode errorCode, String detail, Throwable cause) {
        super(errorCode.getMessage() + " (" + detail + ")", cause);
        this.errorCode = errorCode;
        this.detail = detail;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getDetail() {
        return detail;
    }
}
