package com.themoment.officialgsm.global.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CookieUtil {
    public static void addTokenCookie(HttpServletResponse response, String name, String value, Long maxAge, boolean httpOnly){
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(Math.toIntExact(maxAge));
        cookie.setHttpOnly(httpOnly);
        response.addCookie(cookie);
    }

    public static String getCookieValue(HttpServletRequest request, String name) {
        String value = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    value = cookie.getValue();
                    break;
                }
            }
        }
        return value;
    }
}
