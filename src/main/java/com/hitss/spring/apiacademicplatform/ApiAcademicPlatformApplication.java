package com.hitss.spring.apiacademicplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class ApiAcademicPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiAcademicPlatformApplication.class, args);
    }

}
