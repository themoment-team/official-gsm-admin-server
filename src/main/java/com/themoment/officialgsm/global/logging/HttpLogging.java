package com.themoment.officialgsm.global.logging;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.util.*;

@Slf4j
@Aspect
@Component
public class HttpLogging{

    private final MultipartResolver multipartResolver;

    @Autowired
    public HttpLogging(MultipartResolver multipartResolver) {
        this.multipartResolver = multipartResolver;
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void onRequest() {
    }

    @Around("onRequest()")
    public Object logRequestAndResponse(ProceedingJoinPoint joinPoint) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        UUID code = UUID.randomUUID();

        boolean isHealthCheck = logRequestInfo(request, joinPoint, code);
        ResponseEntity result = (ResponseEntity) joinPoint.proceed();
        if(isHealthCheck == false) {
            logResponseInfo(result, code, request);
        }

        return result;
    }

    private boolean logRequestInfo(HttpServletRequest servletRequest, ProceedingJoinPoint joinPoint, UUID code) throws JsonProcessingException, UnsupportedEncodingException {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Annotation[][] parameterAnnotations = signature.getMethod().getParameterAnnotations();
        RequestInfo requestInfo = extractInfo(servletRequest);

        boolean isHealthCheck = false;
        if(requestInfo.uri().equals("/health")) {
            return !isHealthCheck;
        }

        boolean isBodyCheck = false;

        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation instanceof RequestPart requestPart) {
                    String key = requestPart.value();
                    Object[] args = joinPoint.getArgs();

                    if (key.equals("content"))
                        logContentInfo(requestInfo, code, key, args[i]);
                    else if (key.equals("file"))
                        logFileInfo(requestInfo, code, servletRequest);

                    isBodyCheck = true;
                } else if (annotation instanceof RequestBody){
                    logJsonRequestInfo(requestInfo, code, servletRequest);
                    isBodyCheck = true;
                }
            }
        }

        if(isBodyCheck == false) {
            boolean foundRequestParamOrPathVariable = false;
            for (Annotation[] parameterAnnotation : parameterAnnotations) {
                for (Annotation annotation : parameterAnnotation) {
                    if (annotation instanceof RequestParam || annotation instanceof PathVariable) {
                        logJsonRequestInfo(requestInfo, code, servletRequest);
                        foundRequestParamOrPathVariable = true;
                        break;
                    }
                }
                if (foundRequestParamOrPathVariable) {
                    break;
                }
            }
        }

        return isHealthCheck;
    }

    private RequestInfo extractInfo(HttpServletRequest servletRequest) {
        String ip = servletRequest.getRemoteAddr();
        String method = servletRequest.getMethod();
        String uri = servletRequest.getRequestURI();
        String params = servletRequest.getQueryString();
        String userAgent = servletRequest.getHeader("User-Agent");
        String accessToken = null;

        Cookie[] cookies = servletRequest.getCookies();
        if (cookies != null) {
            accessToken = Arrays.stream(cookies)
                    .filter(cookie -> "access_token".equals(cookie.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        }

        return new RequestInfo(ip, method, uri, params, userAgent, accessToken);
    }

    private void logContentInfo(RequestInfo reqInfo, UUID code, String key, Object args) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String json = objectMapper.writeValueAsString(args);

        log.info(
                """
                [{}]
                [Request:{}]  Uri: {}, Params: {},
                [Key: {}, Value: {}],
                Ip: {}, User-Agent: {}, Access_token: {}
                """,
                code,
                reqInfo.method(),  reqInfo.uri(), reqInfo.params(),
                key, json,
                reqInfo.ip(), reqInfo.userAgent(), reqInfo.accessToken()
        );
    }

    private void logFileInfo(RequestInfo reqInfo, UUID code, HttpServletRequest servletRequest) {
        MultipartHttpServletRequest multipartRequest = multipartResolver.resolveMultipart(servletRequest);
        Map<String, List<MultipartFile>> fileMap = multipartRequest.getMultiFileMap();

        for (Map.Entry<String, List<MultipartFile>> entry : fileMap.entrySet()) {
            String key = entry.getKey();
            List<MultipartFile> files = entry.getValue();

            for (MultipartFile file : files) {
                log.info(
                        """
                        [{}]
                        [Request:{}]  Uri: {}, Params: {},
                        [Key: {}, Filename: {}, ContentType: {}, Size: {}KB],
                        Ip: {}, User-Agent: {}, Access_token: {}
                        """,
                        code,
                        reqInfo.method(),  reqInfo. uri(), reqInfo.params(),
                        key, file.getOriginalFilename(), file.getContentType(), file.getSize()/1024,
                        reqInfo.ip(), reqInfo.userAgent(), reqInfo.accessToken()
                );
            }
        }
    }

    private void logJsonRequestInfo(RequestInfo reqInfo, UUID code, HttpServletRequest servletRequest) throws UnsupportedEncodingException {
        ContentCachingRequestWrapper cachedRequest = (ContentCachingRequestWrapper) servletRequest.getAttribute("cachedRequest");
        String requestBodyJson = new String(cachedRequest.getContentAsByteArray(), servletRequest.getCharacterEncoding()).replaceAll("\\s+", "");

        log.info(
                """
                [{}]
                [Request:{}]  Uri: {}, Params: {},
                Body: {},
                Ip: {}, User-Agent: {}, Access_token: {}
                """,
                code,
                reqInfo.method(),  reqInfo. uri(), reqInfo.params(),
                requestBodyJson,
                reqInfo.ip(), reqInfo.userAgent(), reqInfo.accessToken()
        );
    }

    private void logResponseInfo(ResponseEntity result, UUID code, HttpServletRequest servletRequest) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String responseBodyJson = objectMapper.writeValueAsString(result.getBody());

        log.info(
                """
                [{}]
                [Response:{}] Status-Code: {},
                Body: {}
                """,
                code,
                servletRequest.getMethod(), result.getStatusCode(),
                responseBodyJson
        );
    }

}

