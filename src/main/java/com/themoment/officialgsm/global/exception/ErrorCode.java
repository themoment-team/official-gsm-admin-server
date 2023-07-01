package com.themoment.officialgsm.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_REFRESH_TOKEN("유효하지 않은 리프레시 토큰입니다. ", 400),
    REFRESH_TOKEN_EXPIRED("리프레시 토큰이 만료되었습니다.", 400),
    TOKEN_EXPIRED("토큰이 만료되었습니다.", 401),
    UNKNOWN_ERROR("알 수 없는 에러입니다.", 500),
    INVALID_TOKEN("유효하지 않은 토큰입니다.", 401);

    private final String message;
    private final int status;
}
