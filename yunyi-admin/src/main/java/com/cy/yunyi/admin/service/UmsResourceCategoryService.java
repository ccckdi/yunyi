package com.cy.yunyi.admin.service;


import com.cy.yunyi.model.UmsResourceCategory;

import java.util.List;

/**
 * @Author: chx
 * @Description: 后台资源分类管理Service
 * @DateTime: 2021/11/24 16:31
 **/
public interface UmsResourceCategoryService {

    /**
     * 获取所有资源分类
     */
    List<UmsResourceCategory> listAll();

    /**
     * 创建资源分类
     */
    int create(UmsResourceCategory umsResourceCategory);

    /**
     * 修改资源分类
     */
    int update(Long id, UmsResourceCategory umsResourceCategory);

    /**
     * 删除资源分类
     */
    int delete(Long id);
}
