package com.cy.yunyi.portal.vo;

import com.cy.yunyi.model.*;
import lombok.Data;

import java.util.List;

/**
 * @Author: chx
 * @Description: 商品详情VO
 * @DateTime: 2021/12/16 9:33
 **/
@Data
public class GoodsInfoVo {

    //商品信息
    private PmsGoods info;

    //商品属性
    private List<PmsGoodsAttribute> attributeList;

    //商品规格
    private List<GoodsSpecificationVo> specificationList;

    //商品品牌
    private PmsBrand brand;

    //商品产品
    private List<PmsGoodsProduct>  productList;

    //常见问题
    private List<BmsIssue> issueList;
}
