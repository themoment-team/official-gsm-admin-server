package com.themoment.officialgsm.global.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
public class ErrorResponse {
    private final String message;
    private final Integer status;
    private final String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd / HH : mm : ss"));

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode){
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.builder()
                        .message(errorCode.getMessage())
                        .status(errorCode.getStatus())
                        .build());
    }
}
