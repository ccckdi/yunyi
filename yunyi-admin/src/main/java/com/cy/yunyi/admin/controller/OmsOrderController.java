package com.cy.yunyi.admin.controller;

import com.cy.yunyi.admin.service.OmsOrderService;
import com.cy.yunyi.common.api.CommonPage;
import com.cy.yunyi.common.api.CommonResult;
import com.cy.yunyi.model.OmsOrder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* @Author: chx
* @Description: 订单管理Controller
* @DateTime: 2021/12/2 20:35
**/
@RestController
@Api(tags = "OmsOrderController", description = "订单管理")
@RequestMapping("/order")
public class OmsOrderController {

    @Autowired
    private OmsOrderService orderService;

    @ApiOperation("根据订单名称分页获取订单列表")
    @GetMapping("list")
    public CommonResult<CommonPage<OmsOrder>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                  @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<OmsOrder> orderList = orderService.list(keyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(orderList));
    }

}
