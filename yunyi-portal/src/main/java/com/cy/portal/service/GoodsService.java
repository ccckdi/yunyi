package com.cy.portal.service;

import com.cy.portal.vo.GoodsSpecificationVo;
import com.cy.yunyi.model.*;

import java.util.List;

/**
 * @Author: chx
 * @Description: 商品信息Service
 * @DateTime: 2021/12/16 10:21
 **/
public interface GoodsService {

    //获取商品总数
    Long getGoodsCount();

    //根据分类id获取商品列表
    List<PmsGoods> getGoodsListByCategoryId(Long id, Integer pageNum, Integer pageSize);

    //获取商品详情
    PmsGoods getById(Long id);

    //获取商品属性
    List<PmsGoodsAttribute> getAttributeById(Long id);

    //获取商品规格
    List<GoodsSpecificationVo> getSpecificationById(Long id);

    //获取商品品牌
    PmsBrand getBrandById(Long id);

    //获取商品产品
    List<PmsGoodsProduct> getProductById(Long id);
}
