package com.cy.portal.service;

import com.cy.portal.vo.CategoryContentVo;
import com.cy.yunyi.model.PmsCategory;

import java.util.List;

/**
 * @Author: chx
 * @Description: 分类管理Service
 * @DateTime: 2021/12/15 23:18
 **/
public interface CategoryService {

    /**
     * 获取分类内容信息
     */
    CategoryContentVo content();

    /**
     * 根据id获取当前分类
     */
    PmsCategory getCategoryById(Long id);

    /**
     * 根据id获取当前分类的子类
     */
    List<PmsCategory> getSubCategoryById(Long id);
}
