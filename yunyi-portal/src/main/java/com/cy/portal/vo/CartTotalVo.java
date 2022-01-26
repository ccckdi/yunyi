package com.cy.portal.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: chx
 * @Description: TODO
 * @DateTime: 2022/1/17 21:23
 **/
@Data
public class CartTotalVo {
    //总数量
    private Integer goodsCount = 0;
    //总价格
    private BigDecimal goodsAmount = new BigDecimal(0.00);
    //已勾选数量
    private Integer checkedGoodsCount = 0;
    //已勾选价格
    private BigDecimal checkedGoodsAmount = new BigDecimal(0.00);
}
