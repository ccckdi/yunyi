package com.cy.yunyi.portal.controller;

import com.cy.yunyi.portal.annotation.LoginUser;
import com.cy.yunyi.portal.service.UserService;
import com.cy.yunyi.portal.vo.UserContentVo;
import com.cy.yunyi.common.api.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author chx
 * @Description: 个人中心Controller
 * @DateTime 2022/1/26
 */
@RestController
@Api(tags = "UserController", description = "个人中心Controller")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation("个人中心内容")
    @GetMapping("/index")
    public Object index(@LoginUser Long userId) {
        if (userId == null) {
            return CommonResult.unauthorized();
        }

        UserContentVo content = userService.content(userId);
        return CommonResult.success(content);
    }
}
