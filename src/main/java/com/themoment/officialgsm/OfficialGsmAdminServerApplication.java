package com.themoment.officialgsm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class OfficialGsmAdminServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OfficialGsmAdminServerApplication.class, args);
    }

}
