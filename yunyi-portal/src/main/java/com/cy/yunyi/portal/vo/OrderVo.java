package com.cy.yunyi.portal.vo;

import com.cy.yunyi.portal.util.OrderHandleOption;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author caihx
 * @Description: 订单表VO
 * @Date 2022/1/27
 */
@Data
public class OrderVo {
    private Long id;
    private String orderSn;
    private BigDecimal actualPrice;
    private String orderStatusText;
    private OrderHandleOption handleOption;
    private Integer aftersaleStatus;
    private List<OrderGoodsVo> orderGoodsVoList;
}
