package com.cy.yunyi.portal.controller;

import com.cy.yunyi.portal.dto.UserInfo;
import com.cy.yunyi.portal.dto.WxLoginInfo;
import com.cy.yunyi.portal.service.UmsMemberService;
import com.cy.yunyi.common.api.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: chx
 * @Description: 会员登陆注册Controller
 * @DateTime: 2021/12/11 14:19
 **/
@RestController
@Api(tags = "UmsAuthController", description = "会员登录注册")
@RequestMapping("/auth")
public class UmsAuthController {
    @Autowired
    private UmsMemberService memberService;

    @ApiOperation("会员注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult register(@RequestParam(value = "username") String username,
                                 @RequestParam(value = "password") String password,
                                 @RequestParam(value = "telephone") String telephone,
                                 @RequestParam(value = "authCode") String authCode,
                                 @RequestParam(value = "wxCode") String wxCode) {
        CommonResult register = null;

        if (StringUtils.isEmpty(wxCode)){
            register = memberService.register(username, password, telephone, authCode);
        }else {
            //微信小程序注册
            register = memberService.register(username, password, telephone, authCode, wxCode);
        }
        return register;
    }

    @ApiOperation("会员登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult login(@RequestParam String username,
                              @RequestParam String password) {
        return memberService.login(username, password);
    }

    /**
     * 微信登录
     *
     * @param wxLoginInfo 请求内容，{ code: xxx, userInfo: xxx }
     * @param request     请求对象
     * @return 登录结果
     */
    @ApiOperation("微信登录")
    @PostMapping("loginByWeixin")
    public CommonResult loginByWeixin(@RequestBody WxLoginInfo wxLoginInfo, HttpServletRequest request) {
        String code = wxLoginInfo.getCode();
        UserInfo userInfo = wxLoginInfo.getUserInfo();
        if (code == null || userInfo == null) {
            return CommonResult.validateFailed();
        }
        return memberService.loginByWeixin(request, code, userInfo);
    }

    @ApiOperation("获取验证码")
    @RequestMapping(value = "/getAuthCode", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getAuthCode(@RequestParam String telephone) {
        String authCode = memberService.generateAuthCode(telephone);
        System.out.println(authCode);
        return CommonResult.success("获取验证码成功");
    }
}
