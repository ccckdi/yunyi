package com.cy.yunyi.admin.controller;

import com.cy.yunyi.admin.dto.UpdateUserPasswordParam;
import com.cy.yunyi.admin.service.UmsMemberService;
import com.cy.yunyi.common.api.CommonPage;
import com.cy.yunyi.common.api.CommonResult;
import com.cy.yunyi.model.UmsMember;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: chx
 * @Description: 商城用户管理
 * @DateTime: 2021/11/19 23:04
 **/
@RestController
@Api(tags = "UmsMemberController", description = "商城用户管理")
@RequestMapping("/user")
public class UmsMemberController {

    @Autowired
    private UmsMemberService memberService;

    @ApiOperation("根据用户名分页获取用户列表")
    @GetMapping("/list")
    public CommonResult<CommonPage<UmsMember>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                    @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<UmsMember> umsUserList = memberService.list(keyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(umsUserList));
    }

    @ApiOperation("获取指定用户信息")
    @GetMapping("/{id}")
    public CommonResult<UmsMember> getUser(@PathVariable Long id) {
        UmsMember user = memberService.getUser(id);
        return CommonResult.success(user);
    }

    @ApiOperation("修改指定用户信息")
    @PostMapping("/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody UmsMember user) {
        int count = memberService.update(id, user);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("修改指定用户密码")
    @PostMapping("/updatePassword")
    public CommonResult updatePassword(@RequestBody UpdateUserPasswordParam updateUserPasswordParam) {
        int status = memberService.updatePassword(updateUserPasswordParam);
        if (status > 0) {
            return CommonResult.success(status);
        } else if (status == -1) {
            return CommonResult.failed("提交参数不合法");
        } else if (status == -2) {
            return CommonResult.failed("找不到该管理员");
        } else if (status == -3) {
            return CommonResult.failed("旧密码错误");
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("修改帐号状态")
    @PostMapping("/updateStatus/{id}")
    public CommonResult updateStatus(@PathVariable Long id,@RequestParam(value = "status") Integer status) {
        UmsMember umsUser = new UmsMember();
        umsUser.setStatus(status);
        int count = memberService.update(id,umsUser);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("删除指定用户")
    @PostMapping("/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        int count = memberService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}
