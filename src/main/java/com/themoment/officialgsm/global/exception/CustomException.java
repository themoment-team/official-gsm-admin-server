package com.themoment.officialgsm.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException{
    private final String detailMessage;
    private final HttpStatus httpStatus;

    public CustomException(String detailMessage, HttpStatus httpStatus) {
        this.detailMessage = detailMessage;
        this.httpStatus = httpStatus;
    }

    public CustomException(String detailMessage, HttpStatus httpStatus, Throwable cause) {
        super(cause);
        this.detailMessage = detailMessage;
        this.httpStatus = httpStatus;
    }
}
