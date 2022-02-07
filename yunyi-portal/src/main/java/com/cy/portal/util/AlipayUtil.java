package com.cy.portal.util;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.cy.portal.dto.PayVo;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author caihx
 * @Description: 支付宝支付工具类
 * @Date 2022/1/28
 */
@Data
@Component
public class AlipayUtil {
    /** 在支付宝创建的应用ID */
    @Value("${pay.alibaba.appid}")
    private String appId;
    /** 商户私钥 */
    @Value("${pay.alibaba.merchantPrivateKey}")
    private String merchantPrivateKey;
    /** 支付宝公钥 - 查看地址： https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥 */
    @Value("${pay.alibaba.alipayPublicKey}")
    private  String alipayPublicKey;
    // 服务器[异步通知]页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    /**  支付宝会悄悄的给我们发送一个请求，告诉我们支付成功的信息 */
    @Value("${pay.alibaba.notifyUrl}")
    private  String notifyUrl;
    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    /** 同步通知，支付成功，一般跳转到成功页 */
    @Value("${pay.alibaba.returnUrl}")
    private  String returnUrl;
    /** 签名方式 */
    @Value("${pay.alibaba.signType}")
    private  String signType;
    /** 字符编码格式 */
    @Value("${pay.alibaba.charset}")
    private  String charset;
    /** 订单失效时间 - 30分钟 */
    @Value("${pay.alibaba.timeout}")
    private String timeout;
    /** 支付宝网关； https://openapi.alipaydev.com/gateway.do */
    @Value("${pay.alibaba.gatewayUrl}")
    private  String gatewayUrl;

    /**
     * 支付
     * @param vo 支付信息
     * @return 响应页面
     * @throws AlipayApiException
     */
    public String pay(PayVo vo) throws AlipayApiException {
        // 1、根据支付宝的配置生成一个支付客户端
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl,
                appId, merchantPrivateKey, "json",
                charset, alipayPublicKey, signType);
        // 2、创建一个支付请求 - 设置请求参数
        AlipayTradePagePayRequest aliPayRequest = new AlipayTradePagePayRequest();
        //aliPayRequest.setReturnUrl(returnUrl);
        aliPayRequest.setNotifyUrl(notifyUrl);
        // 商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = vo.getOutTradeNo();
        // 付款金额，必填
        String total_amount = vo.getTotalAmount();
        // 订单名称，必填
        String subject = vo.getSubject();
        // 商品描述，可空
        String body = vo.getBody();
        aliPayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"timeout_express\":\""+timeout+"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        // 会收到支付宝的响应，响应的是一个页面，只要浏览器显示这个页面，就会自动来到支付宝的收银台页面
        return alipayClient.pageExecute(aliPayRequest).getBody();
    }
}
