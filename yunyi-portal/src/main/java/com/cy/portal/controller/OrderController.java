package com.cy.portal.controller;

import com.cy.portal.annotation.LoginUser;
import com.cy.portal.dto.SubmitOrderDto;
import com.cy.portal.service.OrderService;
import com.cy.yunyi.common.api.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: chx
 * @Description: 订单管理Controller
 * @DateTime: 2021/12/29 23:27
 **/
@RestController
@Api(tags = "OrderController", description = "订单管理")
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 提交订单
     *
     * @param userId 用户ID
     * @param submitOrderDto   订单信息，{ cartId：xxx, addressId: xxx, couponId: xxx, message: xxx, grouponRulesId: xxx,  grouponLinkId: xxx}
     * @return 提交订单操作结果
     */
    @ApiOperation("提交订单")
    @PostMapping("/submit")
    public CommonResult submit(@LoginUser Long userId, @RequestBody SubmitOrderDto submitOrderDto) {
        if (userId == null) {
            return CommonResult.validateFailed();
        }
        if (submitOrderDto.getCartId() == null || submitOrderDto.getAddressId() == null) {
            return CommonResult.validateFailed();
        }
        orderService.submit(userId, submitOrderDto);
        return CommonResult.success();
    }
}
