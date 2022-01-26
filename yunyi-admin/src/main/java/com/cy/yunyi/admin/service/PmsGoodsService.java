package com.cy.yunyi.admin.service;

import com.cy.yunyi.admin.dto.PmsGoodsAllParam;
import com.cy.yunyi.common.api.CommonResult;
import com.cy.yunyi.model.PmsGoods;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: chx
 * @Description: 商品Service
 * @DateTime: 2021/12/2 16:37
 **/
public interface PmsGoodsService {

    @Transactional
    CommonResult create(PmsGoodsAllParam goodsAllParam);

    @Transactional
    CommonResult update(Long id, PmsGoodsAllParam goodsAllParam);

    int update(Long id, PmsGoods goods);

    List<PmsGoods> list(String keyword, String productSn, Long brandId,Long productCategoryId, Integer publishStatus, Integer pageSize, Integer pageNum);

    List<PmsGoods> listByName(String name);

    @Transactional
    int delete(Long id);

    PmsGoodsAllParam updateInfo(Long id);
}
