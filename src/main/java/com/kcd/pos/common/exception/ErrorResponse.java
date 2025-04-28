package com.kcd.pos.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private String errCd;
    private String errMsg;
    private LocalDateTime timestamp;

    public static ErrorResponse of(String errCd, String errMsg){
        return new ErrorResponse(errCd, errMsg, LocalDateTime.now(ZoneId.of("Asia/Seoul")));
    }
}
