package com.cy.yunyi.admin.service;

import com.cy.yunyi.model.PmsGoodsSpecification;

import java.util.List;

/**
 * @Author: chx
 * @Description: 商品库存管理Service
 * @DateTime: 2021/12/1 10:35
 **/
public interface PmsGoodsSpecificationService {
    int create(PmsGoodsSpecification product);

    int update(Long id, PmsGoodsSpecification product);

    List<PmsGoodsSpecification> list(String keyword, Integer pageSize, Integer pageNum);

    //根据商品Id查询
    List<PmsGoodsSpecification> listByGoodId(Long goodsId);

    int delete(Long id);
}
