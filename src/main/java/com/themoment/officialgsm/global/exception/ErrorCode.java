package com.themoment.officialgsm.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNKNOWN_ERROR("알 수 없는 에러입니다.", 500);

    private final String message;
    private final Integer status;
}
