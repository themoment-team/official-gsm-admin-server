package com.themoment.officialgsm.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    UNKNOWN_ERROR(500, "알 수 없는 에러입니다."),
    USER_NOT_FOUND(404, "존재하지 않는 유저입니다."),
    FILE_UPLOAD_ERROR(400, "파일 업로드 에러"),
    WRONG_INPUT_FILE(400, "잘못된 파일입니다."),
    POST_NOT_FOUND(404, "존재하지 않는 게시글입니다.");

    private final int status;
    private final String message;
}
