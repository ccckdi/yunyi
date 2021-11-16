package com.cy.yunyi.auth.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: chx
 * @Description: TODO
 * @DateTime: 2021/11/10 20:42
 **/
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/hello")
    public String hello(){
        return "hello security";
    }

    @GetMapping("/index")
    @PreAuthorize("hasAnyAuthority('admin')")
    public String index(){
        return "index";
    }

    @GetMapping("/update")
    @Secured({"ROLE_admin"})
    public String update(){
        return "update";
    }
}
