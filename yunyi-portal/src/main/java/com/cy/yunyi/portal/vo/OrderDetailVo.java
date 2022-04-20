package com.cy.yunyi.portal.vo;

import com.cy.yunyi.portal.util.OrderHandleOption;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author caihx
 * @Description: 订单VO
 * @Date 2022/2/9
 */
@Data
public class OrderDetailVo {
    private Long id;
    private String orderSn;
    private String message;
    private Date createTime;
    private String consignee;
    private String mobile;
    private String address;
    private BigDecimal goodsPrice;
    private BigDecimal couponPrice;
    private BigDecimal freightPrice;
    private BigDecimal actualPrice;
    private String orderStatusText;
    private OrderHandleOption handleOption;
    private Integer aftersaleStatus;
    private String expCode;
    private String expName;
    private String expNo;
}
