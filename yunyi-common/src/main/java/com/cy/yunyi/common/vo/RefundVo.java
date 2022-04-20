package com.cy.yunyi.common.vo;

import lombok.Data;

/**
 * @Author caihx
 * @Description: 阿里退款Vo
 * @Date 2022/4/18
 */
@Data
public class RefundVo {
    private String tradeNo;
    private String refundAmount;
    private String outRequestNo;
}
