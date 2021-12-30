package com.cy.portal.controller;

import com.cy.portal.service.HomeService;
import com.cy.portal.vo.HomeContentVo;
import com.cy.yunyi.common.api.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: chx
 * @Description: 首页内容管理Controller
 * @DateTime: 2021/12/13 23:30
 **/
@RestController
@Api(tags = "HomeController", description = "首页内容管理")
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @ApiOperation("首页内容页信息展示")
    @GetMapping("/index")
    public CommonResult<HomeContentVo> content() {
        HomeContentVo contentResult = homeService.content();
        return CommonResult.success(contentResult);
    }
}
