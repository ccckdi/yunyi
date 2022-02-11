package com.cy.portal.service;

import com.cy.portal.vo.CategoryContentVo;
import com.cy.yunyi.model.PmsCategory;
import com.cy.yunyi.model.PmsGoods;

import java.util.List;
import java.util.Set;

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

    List<PmsCategory> queryL2ByGoods(List<Long> categoryIdList);

    /**
     * 根据查询条件获取id列表
     */
    List<Long> queryIdsSelective(Long brandId, String keyword, Integer isHot, Integer isNew);
}
