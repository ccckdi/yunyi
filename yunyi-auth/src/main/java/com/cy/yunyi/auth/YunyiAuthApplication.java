package com.cy.yunyi.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@MapperScan("com.cy.yunyi.auth.mapper")
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class YunyiAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(YunyiAuthApplication.class, args);
    }

}
