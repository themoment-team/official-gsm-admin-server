package com.themoment.officialgsm.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    ACCESS_TOKEN_EXPIRED("엑세스 토큰이 만료되었습니다.", 401),
    UNKNOWN_ERROR("알 수 없는 에러입니다.", 500),
    INVALID_TOKEN("유효하지 않은 토큰입니다.",401);

    private final String message;
    private final int status;
}
