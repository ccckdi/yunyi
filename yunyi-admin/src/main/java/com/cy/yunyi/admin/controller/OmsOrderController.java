package com.cy.yunyi.admin.controller;

import com.alipay.api.AlipayApiException;
import com.cy.yunyi.admin.service.OmsOrderService;
import com.cy.yunyi.admin.vo.OmsOrderDetailsVo;
import com.cy.yunyi.common.api.CommonPage;
import com.cy.yunyi.common.api.CommonResult;
import com.cy.yunyi.model.OmsOrder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
* @Author: chx
* @Description: 订单管理Controller
* @DateTime: 2021/12/2 20:35
**/
@RestController
@Api(tags = "OmsOrderController", description = "订单管理")
@RequestMapping("/order")
@Slf4j
public class OmsOrderController {

    @Autowired
    private OmsOrderService orderService;

    @ApiOperation("根据查询参数分页获取订单列表")
    @GetMapping("/list")
    public CommonResult<CommonPage<OmsOrder>> list(@RequestParam(value = "orderSn", required = false) String orderSn,
                                                   @RequestParam(value = "receiverKeyword", required = false) String receiverKeyword,
                                                   @RequestParam(value = "orderStatus", required = false) Integer orderStatus,
                                                   @DateTimeFormat(pattern="yyyy-MM-dd") @RequestParam(value = "createTime", required = false)Date createTime,
                                                   @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                   @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<OmsOrder> orderList = orderService.list(orderSn, receiverKeyword, orderStatus, createTime, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(orderList));
    }

    @ApiOperation("获取订单详情：订单信息、商品信息、操作记录")
    @GetMapping(value = "/{id}")
    public CommonResult<OmsOrderDetailsVo> detail(@PathVariable Long id) {
        OmsOrderDetailsVo orderDetailResult = orderService.detail(id);
        return CommonResult.success(orderDetailResult);
    }

    @ApiOperation("退款订单列表")
    @GetMapping("/refundList")
    public CommonResult<CommonPage<OmsOrder>> refundList(@RequestParam(value = "orderSn", required = false) String orderSn,
                                                   @RequestParam(value = "receiverKeyword", required = false) String receiverKeyword,
                                                   @RequestParam(value = "orderStatus", required = false) Integer orderStatus,
                                                   @DateTimeFormat(pattern="yyyy-MM-dd") @RequestParam(value = "updateTime", required = false)Date updateTime,
                                                   @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                   @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<OmsOrder> orderList = orderService.refundList(orderSn, receiverKeyword, orderStatus, updateTime, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(orderList));
    }

    @ApiOperation("确认退款")
    @PostMapping("/agreeRefund/{id}")
    public CommonResult agreeRefund(@PathVariable Long id) throws AlipayApiException {
        boolean success = orderService.agreeRefund(id);
        if (success) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    @ApiOperation("拒绝退款")
    @PostMapping("/refusedRefund/{id}")
    public CommonResult refusedRefund(@PathVariable Long id) throws AlipayApiException {
        boolean success = orderService.refusedRefund(id);
        if (success) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }
}
