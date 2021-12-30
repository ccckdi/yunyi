package com.cy.portal.vo;

import com.cy.yunyi.model.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: chx
 * @Description: 购物车返回信息封装
 * @DateTime: 2021/12/17 9:14
 **/
@Data
public class CartContentVo {
    //购物车列表
    private List<OmsCart> cartList;
    private Integer goodsCount = 0;
    private BigDecimal goodsAmount = new BigDecimal(0.00);
    private Integer checkedGoodsCount = 0;
    private BigDecimal checkedGoodsAmount = new BigDecimal(0.00);
}
