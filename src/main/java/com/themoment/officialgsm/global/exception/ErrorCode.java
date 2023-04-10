package com.themoment.officialgsm.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    REFRESH_TOKEN_NOTFOUND("리프레시 토큰을 찾을 수 없습니다.", 404),
    WRONG_PASSWORD("잘못된 비밀번호 입니다.", 400),
    USERID_NOT_FOUND("유저 아이디를 찾을 수 없습니다.", 404),
    WRONG_SCHOOL_IP("학교 IP가 아닙니다.", 400),
    USERID_ALREADY_EXIST("이미 존재하는 유저 아이디 입니다.", 409),
    UNKNOWN_ERROR("알 수 없는 에러입니다.", 500),
    USER_NOT_FOUND("유저를 찾을 수 없습니다.", 404),
    TOKEN_EXPIRATION("토큰이 만료되었습니다.", 401),
    TOKEN_NOT_VALID("토큰이 유효하지 않습니다.", 401),
    BLACK_LIST_ALREADY_EXIST("블랙리스트에 이미 존재합니다.", 409);

    private final String message;
    private final Integer status;
}
