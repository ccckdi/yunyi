package com.cy.portal.dao;

import com.cy.yunyi.model.PmsBrand;
import com.cy.yunyi.model.PmsGoodsProduct;
import com.cy.yunyi.model.SmsHomeAdvertise;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: chx
 * @Description: 首页内容管理自定义Dao
 * @DateTime: 2021/12/8 21:39
 **/
public interface HomeDao {
    /**
     * 获取轮播广告
     */
    List<SmsHomeAdvertise> getFlashProductList(@Param("flashPromotionId") Long flashPromotionId, @Param("sessionId") Long sessionId);

    /**
     * 获取新品推荐
     */
    List<PmsGoodsProduct> getNewProductList(@Param("offset") Integer offset, @Param("limit") Integer limit);
    /**
     * 获取人气推荐
     */
    List<PmsGoodsProduct> getHotProductList(@Param("offset") Integer offset,@Param("limit") Integer limit);
}
