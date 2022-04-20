package com.cy.yunyi.portal.vo;

import com.cy.yunyi.model.*;
import lombok.Data;

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

    private CartTotalVo cartTotal;
}
