package com.cy.portal.service.impl;

import com.cy.portal.service.CategoryService;
import com.cy.portal.vo.CategoryContentVo;
import com.cy.yunyi.mapper.PmsCategoryMapper;
import com.cy.yunyi.model.PmsCategory;
import com.cy.yunyi.model.PmsCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: chx
 * @Description: 分类管理Service实现类
 * @DateTime: 2021/12/15 23:23
 **/
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private PmsCategoryMapper categoryMapper;

    @Override
    public CategoryContentVo content() {
        CategoryContentVo contentResult = new CategoryContentVo();

        //获取父分类信息
        PmsCategoryExample example = new PmsCategoryExample();
        example.createCriteria().andLevelEqualTo("L1").andStatusEqualTo(1);
        example.setOrderByClause("sort_order desc");
        List<PmsCategory> categoryList = categoryMapper.selectByExample(example);
        contentResult.setCategoryList(categoryList);

        if (categoryList != null && categoryList.size() > 0){
            PmsCategory currentcategory = categoryList.get(0);
            contentResult.setCurrentCategory(this.getCategoryById(currentcategory.getId()));
            contentResult.setCurrentSubCategoryList(this.getSubCategoryById(currentcategory.getId()));
        }
        return contentResult;
    }

    @Override
    public PmsCategory getCategoryById(Long id) {
        return categoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<PmsCategory> getSubCategoryById(Long id) {
        PmsCategoryExample example = new PmsCategoryExample();
        example.createCriteria().andPidEqualTo(id).andStatusEqualTo(1);
        example.setOrderByClause("sort_order desc");
        List<PmsCategory> categoryList = categoryMapper.selectByExample(example);
        return categoryList;
    }


}
