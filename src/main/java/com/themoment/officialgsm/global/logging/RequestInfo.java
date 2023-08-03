package com.themoment.officialgsm.global.logging;

public record RequestInfo(

        String ip,
        String method,
        String uri,
        String params,
        String userAgent,
        String accessToken
) {}
