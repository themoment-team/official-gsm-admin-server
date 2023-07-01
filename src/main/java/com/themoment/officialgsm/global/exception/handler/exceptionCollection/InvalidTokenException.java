package com.themoment.officialgsm.global.exception.handler.exceptionCollection;

import com.themoment.officialgsm.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class InvalidTokenException extends RuntimeException{
    private final ErrorCode errorCode;

    public InvalidTokenException(String message, ErrorCode errorCode){
        super(message);
        this.errorCode = errorCode;
    }
}
