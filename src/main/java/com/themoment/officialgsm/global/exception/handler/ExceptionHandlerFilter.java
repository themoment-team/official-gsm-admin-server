package com.themoment.officialgsm.global.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.themoment.officialgsm.global.exception.ErrorCode;
import com.themoment.officialgsm.global.exception.ErrorResponse;
import com.themoment.officialgsm.global.exception.handler.exceptionCollection.AccessTokenExpiredException;
import com.themoment.officialgsm.global.exception.handler.exceptionCollection.InvalidTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (InvalidTokenException e){
            errorMessageResponse(response, e.getErrorCode());
        } catch (AccessTokenExpiredException e){
            errorMessageResponse(response, e.getErrorCode());
        } catch (Exception e){
            log.error("알 수 없는 에러 발생", e);
            errorMessageResponse(response, ErrorCode.UNKNOWN_ERROR);
        }
    }

    private void errorMessageResponse(HttpServletResponse response, ErrorCode errorCode){
        response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        response.setStatus(errorCode.getStatus());

        try {
            ErrorResponse errorResponse = new ErrorResponse(errorCode);
            String errorResponseEntityToJson = objectMapper.writeValueAsString(errorResponse);
            response.getWriter().write(errorResponseEntityToJson);
        } catch (IOException e) {
            log.error("Filter에서 ErrorResponse Json 변환 실패", e);
            throw new RuntimeException(e);
        }

    }
}
