package com.cy.yunyi.admin.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.cy.yunyi.admin.service.AdminService;
import com.cy.yunyi.common.domain.UserDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: chx
 * @Description: 后台用户管理
 * @DateTime: 2021/11/19 23:04
 **/
@RestController
@RequestMapping("/admin")
public class UmsAdminController {

    @Autowired
    private AdminService adminService;

    @ApiOperation("根据用户名获取通用用户信息")
    @GetMapping("/loadByUsername")
    public UserDto loadUserByUsername(@RequestParam String username) {
        UserDto userDTO = adminService.loadUserByUsername(username);
        return userDTO;
//        return new UserDto(1L,username, BCrypt.hashpw("123456"),1, "client-app", CollUtil.toList("ADMIN","TEST"));
    }
}
