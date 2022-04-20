package com.cy.yunyi.common.notify;

/**
 * @Author caihx
 * @Description: 发生短信枚举
 * @Date 2022/2/7
 */
public enum NotifyType {
    /*支付通知*/
    PAY_SUCCEED("paySucceed"),
    /*发货通知*/
    SHIP("ship"),
    /*退款通知*/
    REFUND("refund"),
    /*验证码*/
    CAPTCHA("captcha");

    private String type;

    NotifyType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}

