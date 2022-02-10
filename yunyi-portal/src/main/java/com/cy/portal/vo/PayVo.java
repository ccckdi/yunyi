package com.cy.portal.vo;

import lombok.Data;

/**
 * @Author caihx
 * @Description: 阿里支付Vo
 * @Date 2022/2/7
 */
@Data
public class PayVo {
    private String outTradeNo;
    private String totalAmount;
    private String subject;
    private String body;
}
