package com.cy.yunyi.portal.vo;

import com.cy.yunyi.model.PmsBrand;
import com.cy.yunyi.model.PmsCategory;
import com.cy.yunyi.model.PmsGoods;
import com.cy.yunyi.model.SmsHomeAdvertise;
import lombok.Data;

import java.util.List;

/**
 * @Author: chx
 * @Description: 首页内容返回信息封装
 * @DateTime: 2021/12/13 23:33
 **/
@Data
public class HomeContentVo {
    //轮播广告
    private List<SmsHomeAdvertise> advertiseList;
    //分类列表
    private List<PmsCategory> categoryList;
    //品牌列表
    private List<PmsBrand> brandList;
    //新品推荐
    private List<PmsGoods> newGoodsList;
    //人气推荐
    private List<PmsGoods> hotGoodsList;
    //商品总数
    private Long goodsCount;
}
