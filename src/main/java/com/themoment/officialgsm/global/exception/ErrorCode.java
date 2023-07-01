package com.themoment.officialgsm.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_TOKEN("유효하지 않은 토큰입니다.",401);

    private final String message;
    private final int status;
}
