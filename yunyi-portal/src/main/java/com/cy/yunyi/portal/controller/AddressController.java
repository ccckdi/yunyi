package com.cy.yunyi.portal.controller;

import com.cy.yunyi.portal.annotation.LoginUser;
import com.cy.yunyi.portal.service.AddressService;
import com.cy.yunyi.portal.service.RegionService;
import com.cy.yunyi.portal.vo.RegionVo;
import com.cy.yunyi.common.api.CommonResult;
import com.cy.yunyi.model.UmsAddress;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: chx
 * @Description: 收货地址管理Controller
 * @DateTime: 2021/12/28 21:35
 **/
@RestController
@Api(tags = "AddressController", description = "收货地址管理")
@RequestMapping("/address")
public class AddressController {

//    @Autowired
//    private

    @Autowired
    private RegionService regionService;

    @Autowired
    private AddressService addressService;

    @ApiOperation("行政区表")
    @GetMapping("/region")
    public CommonResult region() {
        List<RegionVo> regionVoList = regionService.list();
        return CommonResult.success(regionVoList);
    }

    @ApiOperation("获取收货地址列表")
    @GetMapping("/list")
    public CommonResult list(@LoginUser Long userId) {
        List<UmsAddress> addressList = addressService.list(userId);
        return CommonResult.success(addressList);
    }

    @ApiOperation("获取收货地址详情")
    @GetMapping("/detail")
    public CommonResult detail(Long id) {
        UmsAddress address = addressService.getAddressById(id);
        return CommonResult.success(address);
    }

    /**
     * 添加或更新收货地址
     *
     * @param userId  用户ID
     * @param address 用户收货地址
     * @return 添加或更新操作结果
     */
    @ApiOperation("添加或更新收货地址")
    @PostMapping("/save")
    public CommonResult save(@LoginUser Long userId, @RequestBody UmsAddress address) {
        if (userId == null) {
            return CommonResult.unauthorized();
        }

        int count = 0;

        if (address.getId() == null || address.getId() == 0l) {
            count = addressService.addAddress(userId,address);
        } else {
            count = addressService.updateAddress(userId,address);
        }
        if (count > 0){
            return CommonResult.success();
        }else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("删除收货地址")
    @PostMapping("/delete")
    public CommonResult delete(@LoginUser Long userId, @RequestBody UmsAddress address) {
        return CommonResult.success();
    }
}
