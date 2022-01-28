package com.cy.portal.util;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author caihx
 * @Description: 支付宝支付工具类
 * @Date 2022/1/28
 */
/* 支付宝 */
@Component
public class AlipayUtil {

    //应用id
    @Value("${alipay.appId}")
    private String appId;

    //私钥
    @Value("${alipay.privateKey}")
    private String privateKey;

    //公钥
    @Value("${alipay.publicKey}")
    private String publicKey;

    //支付宝网关
    @Value("${alipay.gateway}")
    private String gateway;

    //支付成功后的接口回调地址，不是回调的友好页面，不要弄混了
    @Value("${alipay.notifyUrl}")
    private String notifyUrl;

    public String doPay() throws AlipayApiException {
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(
                gateway,
                appId,
                privateKey,
                "json",
                "utf-8",
                publicKey,
                "RSA2");

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
//        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        alipayRequest.setNotifyUrl(notifyUrl);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = "12345678";
        //付款金额，必填
        String total_amount = "10";
        //订单名称，必填
        String subject = "云毅GO订单";
        //商品描述，可空
        String body = "商品描述";

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
                + "\"total_amount\":\"" + total_amount + "\","
                + "\"subject\":\"" + subject + "\","
                + "\"body\":\"" + body + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        //若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
        //alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
        //		+ "\"total_amount\":\""+ total_amount +"\","
        //		+ "\"subject\":\""+ subject +"\","
        //		+ "\"body\":\""+ body +"\","
        //		+ "\"timeout_express\":\"10m\","
        //		+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        //请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节

        //请求
        String result = alipayClient.pageExecute(alipayRequest).getBody();

        //输出
        System.out.println(result);
        return result;
    }
}
