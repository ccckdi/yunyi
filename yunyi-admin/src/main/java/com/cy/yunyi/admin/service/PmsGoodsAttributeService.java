package com.cy.yunyi.admin.service;

import com.cy.yunyi.model.PmsGoodsAttribute;

import java.util.List;

/**
 * @Author: chx
 * @Description: 商品参数管理Service
 * @DateTime: 2021/12/1 10:35
 **/
public interface PmsGoodsAttributeService {
    int create(PmsGoodsAttribute attribute);

    int update(Long id, PmsGoodsAttribute attribute);

    List<PmsGoodsAttribute> list(String keyword, Integer pageSize, Integer pageNum);

    //根据商品Id查询
    List<PmsGoodsAttribute> listByGoodId(Long goodsId);

    int delete(Long id);
}
