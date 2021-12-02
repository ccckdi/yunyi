package com.cy.yunyi.admin.dto;

import com.cy.yunyi.model.PmsGoods;
import com.cy.yunyi.model.PmsGoodsAttribute;
import com.cy.yunyi.model.PmsGoodsProduct;
import com.cy.yunyi.model.PmsGoodsSpecification;
import lombok.Data;

/**
 * @Author: chx
 * @Description: 商品1对多关联
 * @DateTime: 2021/12/2 9:18
 **/
@Data
public class PmsGoodsAllParam {
    PmsGoods goods;
    PmsGoodsSpecification[] specifications;
    PmsGoodsAttribute[] attributes;
    PmsGoodsProduct[] products;
}
