package com.cy.yunyi.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@MapperScan("com.cy.yunyi.security.mapper")
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class YunyiSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(YunyiSecurityApplication.class, args);
    }

}
