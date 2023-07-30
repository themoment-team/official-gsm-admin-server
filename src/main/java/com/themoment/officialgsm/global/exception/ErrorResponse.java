package com.themoment.officialgsm.global.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Builder
@RequiredArgsConstructor
@Getter
public class ErrorResponse {

    private final String formatNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    private final String detailMessage;

    public static ResponseEntity<ErrorResponse> toResponseEntity(String detailMessage, HttpStatus httpStatus) {
        return ResponseEntity
                .status(httpStatus)
                .body(ErrorResponse.builder()
                        .detailMessage(detailMessage)
                        .build()
                );
    }
}
