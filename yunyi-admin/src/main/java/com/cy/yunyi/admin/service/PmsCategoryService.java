package com.cy.yunyi.admin.service;

import com.cy.yunyi.model.PmsCategory;

import java.util.List;

/**
 * @Author: chx
 * @Description: 商品分类Service
 * @DateTime: 2021/11/30 16:37
 **/
public interface PmsCategoryService {
    int create(PmsCategory category);

    int update(Long id, PmsCategory category);

    List<PmsCategory> list(String keyword, Integer pageSize, Integer pageNum);

    int delete(Long id);
}
