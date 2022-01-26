package com.cy.portal.service.impl;

import com.cy.portal.service.GoodsProductService;
import com.cy.yunyi.mapper.PmsGoodsMapper;
import com.cy.yunyi.mapper.PmsGoodsProductMapper;
import com.cy.yunyi.model.PmsGoods;
import com.cy.yunyi.model.PmsGoodsProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: chx
 * @Description: 商品产品Service实现类
 * @DateTime: 2022/1/17 20:25
 **/
@Service
public class GoodsProductServiceImpl implements GoodsProductService {

    @Autowired
    PmsGoodsProductMapper productMapper;

    @Override
    public PmsGoodsProduct getById(Long productId) {
        PmsGoodsProduct product = productMapper.selectByPrimaryKey(productId);
        return product;
    }

    @Override
    public int reduceStock(Long productId, Integer number) {
        PmsGoodsProduct product = productMapper.selectByPrimaryKey(productId);
        product.setNumber(number);
        int count = productMapper.updateByPrimaryKey(product);
        return count;
    }
}
