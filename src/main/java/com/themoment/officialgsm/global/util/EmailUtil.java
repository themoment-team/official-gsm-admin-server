package com.themoment.officialgsm.global.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmailUtil {
    public static String getEmailDomain(String email) {
        int index = email.indexOf("@");
        if (index != -1) {
            return email.substring(index + 1);
        }
        return "";
    }
}
