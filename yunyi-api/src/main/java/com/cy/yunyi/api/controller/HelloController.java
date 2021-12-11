package com.cy.yunyi.api.controller;

import com.cy.yunyi.common.exception.Asserts;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: chx
 * @Description: TODO 测试接口
 * @DateTime: 2021/11/17 23:20
 **/
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello World.";
    }

    @GetMapping("/test")
    public String test() {
        Asserts.fail("抛出异常！！！");
        return "Hello World.";
    }
}