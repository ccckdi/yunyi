package com.cy.yunyi.api.controller;

import com.cy.yunyi.api.component.LoginUserHolder;
import com.cy.yunyi.common.domain.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: chx
 * @Description: TODO 获取用户信息接口
 * @DateTime: 2021/11/17 23:24
 **/
@RestController
@RequestMapping("/user")
public class UserController{

    @Autowired
    private LoginUserHolder loginUserHolder;

    @GetMapping("/currentUser")
    public UserDto currentUser() {
        return loginUserHolder.getCurrentUser();
    }

}
