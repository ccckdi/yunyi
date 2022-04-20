package com.cy.yunyi.portal.vo;

import com.cy.yunyi.model.PmsGoodsSpecification;
import lombok.Data;

import java.util.List;

/**
 * @Author: chx
 * @Description: 商品规格Vo
 * @DateTime: 2021/12/16 21:25
 **/
@Data
public class GoodsSpecificationVo {

    //规格名
    private String name;

    //规格值列表
    private List<PmsGoodsSpecification> valueList;
}
