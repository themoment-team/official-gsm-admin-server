package com.themoment.officialgsm;

import com.themoment.officialgsm.global.security.jwt.properties.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({JwtProperties.class})
public class OfficialGsmAdminServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OfficialGsmAdminServerApplication.class, args);
    }

}
