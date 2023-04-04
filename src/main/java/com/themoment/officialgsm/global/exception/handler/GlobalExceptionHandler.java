package com.themoment.officialgsm.global.exception.handler;

import com.themoment.officialgsm.global.exception.CustomException;
import com.themoment.officialgsm.global.exception.ErrorCode;
import com.themoment.officialgsm.global.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handlerCustomException(CustomException e){
        log.error("handleCustomException throw CustomException : {}", e.getErrorCode(), e);
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handlerCustomException(Exception e){
        log.error("UnknownExceptionHandler throw Exception : {}", e.getMessage(), e);
        return ErrorResponse.toResponseEntity(ErrorCode.UNKNOWN_ERROR);
    }
}
