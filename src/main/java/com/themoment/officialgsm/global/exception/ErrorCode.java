package com.themoment.officialgsm.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNKNOWN_ERROR("알 수 없는 에러입니다.", 500),
    USER_NOT_FOUND("유저를 찾을 수 없습니다.", 404),
    TOKEN_EXPIRATION("토큰이 만료되었습니다.", 401),
    TOKEN_NOT_VALID("토큰이 유효하지 않습니다.", 401);

    private final String message;
    private final Integer status;
}
