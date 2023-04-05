package com.themoment.officialgsm.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    UNKNOWN_ERROR(500, "알 수 없는 에러입니다."),
    USER_NOT_FOUND(404, "존재하지 않는 유저입니다.");

    private final int status;
    private final String detail;
}
