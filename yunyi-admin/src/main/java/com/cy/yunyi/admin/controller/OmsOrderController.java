package com.cy.yunyi.admin.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.cy.yunyi.admin.service.OmsOrderService;
import com.cy.yunyi.admin.vo.OmsOrderDetailsVo;
import com.cy.yunyi.common.util.AlipayUtil;
import com.cy.yunyi.common.vo.PayAsyncVo;
import com.cy.yunyi.common.api.CommonPage;
import com.cy.yunyi.common.api.CommonResult;
import com.cy.yunyi.model.OmsOrder;
import com.cy.yunyi.model.PmsBrand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private AlipayUtil alipayUtil;

    @ApiOperation("根据查询参数分页获取订单列表")
    @GetMapping("list")
    public CommonResult<CommonPage<OmsOrder>> list(@RequestParam(value = "orderSn", required = false) String orderSn,
                                                   @RequestParam(value = "receiverKeyword", required = false) String receiverKeyword,
                                                   @RequestParam(value = "status", required = false) Integer status,
                                                   @DateTimeFormat(pattern="yyyy-MM-dd") @RequestParam(value = "createTime", required = false)Date createTime,
                                                   @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                   @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<OmsOrder> orderList = orderService.list(orderSn, receiverKeyword, status, createTime, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(orderList));
    }

    @ApiOperation("获取订单详情：订单信息、商品信息、操作记录")
    @GetMapping(value = "/{id}")
    public CommonResult<OmsOrderDetailsVo> detail(@PathVariable Long id) {
        OmsOrderDetailsVo orderDetailResult = orderService.detail(id);
        return CommonResult.success(orderDetailResult);
    }

    @ApiOperation("退款订单列表")
    @GetMapping("refundList")
    public CommonResult<CommonPage<OmsOrder>> refundList(@RequestParam(value = "orderSn", required = false) String orderSn,
                                                   @RequestParam(value = "receiverKeyword", required = false) String receiverKeyword,
                                                   @RequestParam(value = "status", required = false) Integer status,
                                                   @DateTimeFormat(pattern="yyyy-MM-dd") @RequestParam(value = "createTime", required = false)Date createTime,
                                                   @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                   @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<OmsOrder> orderList = orderService.list(orderSn, receiverKeyword, status, createTime, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(orderList));
    }

    @ApiOperation("确认退款")
    @PostMapping("/refund/{id}")
    public CommonResult refund(@PathVariable Long id) throws AlipayApiException {
        boolean success = orderService.refund(id);
        if (success) {
            return CommonResult.success();
        }
        return CommonResult.failed();
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
