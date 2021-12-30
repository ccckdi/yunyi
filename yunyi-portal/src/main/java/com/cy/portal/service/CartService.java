package com.cy.portal.service;

import com.cy.portal.vo.CartContentVo;
import com.cy.yunyi.model.OmsCart;

import java.util.List;

/**
 * @Author: chx
 * @Description: 购物车Service
 * @DateTime: 2021/12/17 0:24
 **/
public interface CartService {

    /**
     * 查询购物车
     */
    List<OmsCart> listCart(Long userId);

    /**
     * 获取购物车商品数量
     */
    Long getGoodsCount(Long userId);

    /**
     * 添加购物车
     */
    Long addCart(Long userId,OmsCart cart);

    CartContentVo content(Long userId);

    Integer checked(Long userId, Long cartId, Integer isChecked);

    List<OmsCart> getByUserIdAndChecked(Long userId);

    OmsCart getById(Long cartId);
}
