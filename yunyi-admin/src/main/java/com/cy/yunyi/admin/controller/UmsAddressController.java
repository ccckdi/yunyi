package com.cy.yunyi.admin.controller;

import com.cy.yunyi.admin.service.UmsAddressService;
import com.cy.yunyi.common.api.CommonPage;
import com.cy.yunyi.common.api.CommonResult;
import com.cy.yunyi.model.UmsResource;
import com.cy.yunyi.model.UmsAddress;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* @Author: chx
* @Description: 收货地址管理Controller
* @DateTime: 2021/11/25 10:29
**/
@RestController
@Api(tags = "UmsAddressController", description = "后台收货地址管理")
@RequestMapping("/address")
public class UmsAddressController {

    @Autowired
    private UmsAddressService addressService;

    @ApiOperation("根据收货地址分页获取收货地址列表")
    @GetMapping("list")
    public CommonResult<CommonPage<UmsAddress>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                  @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<UmsAddress> addressList = addressService.list(keyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(addressList));
    }
}
