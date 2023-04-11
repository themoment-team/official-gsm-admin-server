package com.themoment.officialgsm.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    UNKNOWN_ERROR(500),
    USER_NOT_FOUND(404),
    FILE_UPLOAD_ERROR(400),
    WRONG_INPUT_FILE(400),
    POST_NOT_FOUND(404);

    private final int status;
}
