package com.themoment.officialgsm.global.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.themoment.officialgsm.global.exception.CustomException;
import com.themoment.officialgsm.global.exception.ErrorResponse;
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
        } catch (CustomException e){
            errorMessageResponse(response, e);
        }
    }

    private void errorMessageResponse(HttpServletResponse response, CustomException exception){
        response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        response.setStatus(exception.getHttpStatus().value());

        try {
            ErrorResponse errorResponse = new ErrorResponse(exception.getDetailMessage());
            String errorResponseEntityToJson = objectMapper.writeValueAsString(errorResponse);
            response.getWriter().write(errorResponseEntityToJson);
        } catch (IOException e) {
            log.error("Filter에서 ErrorResponse Json 변환 실패", e);
            throw new RuntimeException(e);
        }

    }
}
