package com.cy.yunyi.admin.controller;

import com.cy.yunyi.admin.service.SmsHomeAdvertiseService;
import com.cy.yunyi.common.api.CommonPage;
import com.cy.yunyi.common.api.CommonResult;
import com.cy.yunyi.model.SmsHomeAdvertise;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* @Author: chx
* @Description: 广告管理Controller
* @DateTime: 2021/12/1 22:29
**/
@RestController
@Api(tags = "SmsHomeAdvertiseController", description = "广告管理")
@RequestMapping("/advertise")
public class SmsHomeAdvertiseController {

    @Autowired
    private SmsHomeAdvertiseService advertiseService;

    @ApiOperation("添加广告")
    @PostMapping("/create")
    public CommonResult create(@RequestBody SmsHomeAdvertise advertise) {
        int count = advertiseService.create(advertise);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("广告详情")
    @GetMapping("/{id}")
    public CommonResult info(@PathVariable Long id) {
        SmsHomeAdvertise smsHomeAdvertise = advertiseService.info(id);
        return CommonResult.success(smsHomeAdvertise);
    }

    @ApiOperation("修改广告")
    @PostMapping("/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody SmsHomeAdvertise advertise) {
        int count = advertiseService.update(id, advertise);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("根据广告名称分页获取广告列表")
    @GetMapping("list")
    public CommonResult<CommonPage<SmsHomeAdvertise>> list(@RequestParam(value = "name", required = false) String name,
                                                           @RequestParam(value = "type", required = false) Integer type,
                                                           @RequestParam(value = "endTime", required = false) String endTime,
                                                  @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<SmsHomeAdvertise> advertiseList = advertiseService.list(name, type, endTime, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(advertiseList));
    }

    @ApiOperation("修改广告状态")
    @PostMapping("/updateStatus/{id}")
    public CommonResult updateStatus(@PathVariable Long id, @RequestParam(value = "status") Integer status) {
        SmsHomeAdvertise umsRole = new SmsHomeAdvertise();
        umsRole.setStatus(status);
        int count = advertiseService.update(id, umsRole);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("删除广告")
    @PostMapping("/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        int count = advertiseService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}
