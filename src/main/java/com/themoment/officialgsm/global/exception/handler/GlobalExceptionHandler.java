package com.themoment.officialgsm.global.exception.handler;

import com.themoment.officialgsm.global.exception.CustomException;
import com.themoment.officialgsm.global.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        return ErrorResponse.toResponseEntity(e.getDetailMessage(), e.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleNotValidException() {
        return ErrorResponse.toResponseEntity("형식에 맞지 않는 요청입니다.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> noHandlerFoundException(NoHandlerFoundException ex) {
        return ErrorResponse.toResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
