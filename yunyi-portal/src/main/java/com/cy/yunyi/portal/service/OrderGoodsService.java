package com.cy.yunyi.portal.service;

import com.cy.yunyi.model.OmsOrderGoods;

import java.util.List;

/**
 * @Author caihx
 * @Description: 订单商品Service
 * @Date 2022/1/27
 */
public interface OrderGoodsService {

    void create(OmsOrderGoods orderGoods);

    /**
     * 根据订单id查询订单下的商品
     * @param id
     * @return
     */
    List<OmsOrderGoods> queryByOrderId(Long id);
}
