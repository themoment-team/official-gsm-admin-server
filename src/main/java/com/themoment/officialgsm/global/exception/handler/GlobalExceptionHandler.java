package com.themoment.officialgsm.global.exception.handler;

import com.themoment.officialgsm.global.exception.CustomException;
import com.themoment.officialgsm.global.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error("handleCustomException throw CustomException : {}", e.getErrorCode(), e);
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }

}