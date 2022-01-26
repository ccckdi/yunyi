package com.cy.portal.service;

import com.cy.yunyi.model.PmsGoodsProduct;

/**
 * @Author: chx
 * @Description: 商品产品Service
 * @DateTime: 2022/1/17 20:24
 **/
public interface GoodsProductService {

    /**
     * 通过产品id获取产品
     * @param productId
     * @return
     */
    PmsGoodsProduct getById(Long productId);

    /**
     * 根据id扣库存
     * @param productId
     * @param number
     * @return
     */
    int reduceStock(Long productId, Integer number);
}
