package com.cy.portal.controller;

import com.cy.portal.annotation.LoginUser;
import com.cy.portal.dto.SubmitOrderDto;
import com.cy.portal.service.OrderService;
import com.cy.portal.vo.OrderVo;
import com.cy.yunyi.common.api.CommonPage;
import com.cy.yunyi.common.api.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * 订单列表
     *
     * @param userId   用户ID
     * @param showType 订单信息：
     *                 0，全部订单；
     *                 1，待付款；
     *                 2，待发货；
     *                 3，待收货；
     *                 4，待评价。
     * @param pageSize     分页页数
     * @param pageNum     分页大小
     * @return 订单列表
     */
    @ApiOperation("获取我的订单")
    @GetMapping("/list")
    public CommonResult<CommonPage<OrderVo>> list(@LoginUser Long userId,
                                                  @RequestParam(value = "showType", defaultValue = "0") Integer showType,
                                                  @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        if (userId == null) {
            return CommonResult.unauthorized();
        }
        List<OrderVo> orderVoList = orderService.list(userId, showType, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(orderVoList));
    }
}
