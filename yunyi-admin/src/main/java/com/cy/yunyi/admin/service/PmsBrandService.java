package com.cy.yunyi.admin.service;

import com.cy.yunyi.model.PmsBrand;

import java.util.List;

/**
 * @Author: chx
 * @Description: 商品品牌管理Service
 * @DateTime: 2021/11/30 11:00
 **/
public interface PmsBrandService {
    int create(PmsBrand brand);

    int update(Long id, PmsBrand brand);

    List<PmsBrand> list(String keyword, Integer pageSize, Integer pageNum);

    int delete(Long id);
}
