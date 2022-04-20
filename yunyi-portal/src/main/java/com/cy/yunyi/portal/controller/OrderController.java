package com.cy.yunyi.portal.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.cy.yunyi.portal.annotation.LoginUser;
import com.cy.yunyi.common.vo.PayAsyncVo;
import com.cy.yunyi.portal.dto.SubmitOrderDto;
import com.cy.yunyi.portal.service.OrderService;
import com.cy.yunyi.common.util.AlipayUtil;
import com.cy.yunyi.portal.vo.OrderVo;
import com.cy.yunyi.common.api.CommonPage;
import com.cy.yunyi.common.api.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Author: chx
 * @Description: 订单管理Controller
 * @DateTime: 2021/12/29 23:27
 **/
@RestController
@Api(tags = "OrderController", description = "订单管理")
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AlipayUtil alipayUtil;

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
        String orderSn = orderService.submit(userId, submitOrderDto);
        return CommonResult.success(orderSn);
    }

    /**
     * 订单详情
     *
     * @param userId  用户ID
     * @param orderId 订单ID
     * @return 订单详情
     */
    @ApiOperation("订单详情")
    @GetMapping("/detail")
    public CommonResult detail(@LoginUser Long userId, @NotNull Long orderId) {
        if (userId == null) {
            return CommonResult.validateFailed();
        }

        Map<String,Object> result = orderService.detail(userId, orderId);
        return CommonResult.success(result);
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

        Map<String, Object> map = orderService.list(userId, showType, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPageByList((List<OrderVo>) map.get("list"),(List) map.get("pageList")));
    }

    /**
     * 取消订单
     *
     * @param userId 用户ID
     * @return 订单操作结果
     */
    @ApiOperation("取消订单")
    @PostMapping("/cancel")
    public CommonResult cancel(@LoginUser Long userId, @RequestParam(value = "orderId") Long orderId) {
        if (userId == null) {
            return CommonResult.validateFailed();
        }

        Integer result = orderService.cancel(userId, orderId);
        if (result > 0){
            return CommonResult.success();
        }else if (result == -1){
            return CommonResult.failed("订单不能取消！");
        } else {
            return CommonResult.failed("修改订单状态失败！");
        }
    }

    /**
     * 去付款
     *
     * @param userId 用户ID
     * @return 付款链接
     */
    @ApiOperation("去付款")
    @PostMapping("/prepay")
    public CommonResult prepay(@LoginUser Long userId, @RequestParam(value = "orderId") Long orderId) {
        if (userId == null) {
            return CommonResult.validateFailed();
        }

        String orderSn = orderService.prepay(userId, orderId);
        if (!StringUtil.isEmpty(orderSn)){
            return CommonResult.success(orderSn);
        }else {
            return CommonResult.failed("订单状态异常，请咨询管理员！");
        }
    }

    @ApiOperation("阿里支付")
    @GetMapping(value = "/aliPay", produces = "text/html")
    public String aliPay(@RequestParam(value = "orderSn") String orderSn) throws AlipayApiException {
        return orderService.aliPay(orderSn);
    }

    @ApiOperation("支付回调")
    @PostMapping(value = "/payNotify")
    public CommonResult payNotify(PayAsyncVo vo, HttpServletRequest request) throws AlipayApiException {
        // 验证签名
        boolean signVerified = checkV1(request);
        if(signVerified){
            // 签名验证通过
            log.info("签名验证成功！");
            System.out.println(vo);
            // 处理支付结果
            // 只要收到了支付宝给我们异步的通知，告诉我们订单支付成功，返回success，支付宝就不会再通知
            Integer count = orderService.payNotify(vo);

            if (count > 0){
                return CommonResult.success();
            }else {
                return CommonResult.failed("修改订单状态失败！");
            }
        }else{
            // 只要回复的不是success，就会一直通知
            log.info("签名验证失败！");
            return CommonResult.failed("签名验证失败！");
        }
    }

    /**
     * 申请退款
     *
     * @param userId 用户ID
     * @return 订单操作结果
     */
    @ApiOperation("申请退款")
    @PostMapping("/refund")
    public CommonResult refund(@LoginUser Long userId, @RequestParam(value = "orderId") Long orderId) {
        if (userId == null) {
            return CommonResult.validateFailed();
        }

        Integer result = orderService.refund(userId, orderId);
        if (result > 0){
            return CommonResult.success();
        }else if (result == -1){
            return CommonResult.failed("订单不能退款！");
        } else {
            return CommonResult.failed("修改订单状态失败！");
        }
    }

    /**
     * 确认收货
     *
     * @param userId 用户ID
     * @return 订单操作结果
     */
    @ApiOperation("确认收货")
    @PostMapping("/confirm")
    public CommonResult confirm(@LoginUser Long userId, @RequestParam(value = "orderId") Long orderId) {
        if (userId == null) {
            return CommonResult.validateFailed();
        }

        Integer result = orderService.confirm(userId, orderId);
        if (result > 0){
            return CommonResult.success();
        }else if (result == -1){
            return CommonResult.failed("订单不能确认收货！");
        } else {
            return CommonResult.failed("修改订单状态失败！");
        }
    }

    /**
     * 取消订单
     *
     * @param userId 用户ID
     * @return 订单操作结果
     */
    @ApiOperation("取消订单")
    @PostMapping("/delete")
    public CommonResult delete(@LoginUser Long userId, @RequestParam(value = "orderId") Long orderId) {
        if (userId == null) {
            return CommonResult.validateFailed();
        }

        Integer result = orderService.delete(userId, orderId);
        if (result > 0){
            return CommonResult.success();
        }else if (result == -1){
            return CommonResult.failed("订单不能删除！");
        } else {
            return CommonResult.failed("删除订单失败！");
        }
    }

    /**
     * 校验签名
     * @param request request
     * @return 是否验证通过
     * @throws AlipayApiException 支付异常
     */
    private boolean checkV1(HttpServletRequest request) throws AlipayApiException {
        /*
         * 支付宝验证签名
         * 获取支付宝POST过来反馈信息
         */
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        // 调用SDK验证签名
        return AlipaySignature.rsaCheckV1(params, alipayUtil.getAlipayPublicKey(), alipayUtil.getCharset(), alipayUtil.getSignType());
    }
}
