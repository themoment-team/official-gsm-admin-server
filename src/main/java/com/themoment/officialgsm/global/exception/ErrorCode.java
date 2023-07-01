package com.themoment.officialgsm.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_REFRESH_TOKEN("Invalid Refresh Token", 400),
    REFRESH_TOKEN_EXPIRED("Refresh Token Expired", 400),
    TOKEN_EXPIRED("Token Expired", 401),
    UNKNOWN_ERROR("Unknown Error", 500),
    INVALID_TOKEN("Invalid Token", 401);

    private final String message;
    private final int status;
}
