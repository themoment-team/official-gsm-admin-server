package com.themoment.officialgsm.global.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

@Slf4j
public class EmailUtil {

    @Value("${emailId-regex}")
    String emailIdRegex;

    public String getEmailDomain(String email) {
        int index = email.indexOf("@");
        String emailId = email.substring(0, index);

        if (emailId.matches(emailIdRegex)){
            throw new OAuth2AuthenticationException("학생은 로그인할 수 없습니다.");
        }

        return email.substring(index + 1);
    }
}
