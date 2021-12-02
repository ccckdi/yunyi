package com.cy.yunyi.admin.service;

import com.cy.yunyi.model.PmsGoodsProduct;

import java.util.List;

/**
 * @Author: chx
 * @Description: 商品库存管理Service
 * @DateTime: 2021/12/1 10:35
 **/
public interface PmsGoodsProductService {
    int create(PmsGoodsProduct product);

    int update(Long id, PmsGoodsProduct product);

    List<PmsGoodsProduct> list(String keyword, Integer pageSize, Integer pageNum);

    //根据商品Id查询
    List<PmsGoodsProduct> listByGoodId(Long goodsId);

    int delete(Long id);
}
