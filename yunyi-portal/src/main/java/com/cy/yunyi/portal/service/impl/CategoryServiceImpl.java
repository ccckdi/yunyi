package com.cy.yunyi.portal.service.impl;

import com.cy.yunyi.portal.service.CategoryService;
import com.cy.yunyi.portal.vo.CategoryContentVo;
import com.cy.yunyi.mapper.PmsCategoryMapper;
import com.cy.yunyi.mapper.PmsGoodsMapper;
import com.cy.yunyi.model.PmsCategory;
import com.cy.yunyi.model.PmsCategoryExample;
import com.cy.yunyi.model.PmsGoods;
import com.cy.yunyi.model.PmsGoodsExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @Author: chx
 * @Description: 分类管理Service实现类
 * @DateTime: 2021/12/15 23:23
 **/
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private PmsCategoryMapper categoryMapper;

    @Autowired
    private PmsGoodsMapper goodsMapper;

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

    @Override
    public List<PmsCategory> queryL2ByGoods(List<Long> categoryIdList) {
        PmsCategoryExample example = new PmsCategoryExample();
        PmsCategoryExample.Criteria criteria = example.createCriteria();
        if (categoryIdList != null && categoryIdList.size() != 0 ){
            criteria.andIdIn(categoryIdList);
        }
        criteria.andStatusEqualTo(1);
        List<PmsCategory> categoryList = categoryMapper.selectByExample(example);
        return categoryList;
    }

    @Override
    public List<Long> queryIdsSelective(Long brandId, String keyword, Integer isHot, Integer isNew) {
        PmsGoodsExample example = new PmsGoodsExample();
        PmsGoodsExample.Criteria criteria1 = example.or();
        PmsGoodsExample.Criteria criteria2 = example.or();

        if (!StringUtils.isEmpty(keyword)) {
            criteria1.andKeywordsLike("%" + keyword + "%");
            criteria2.andNameLike("%" + keyword + "%");
        }
        if (!StringUtils.isEmpty(brandId)) {
            criteria1.andBrandIdEqualTo(brandId);
            criteria2.andBrandIdEqualTo(brandId);
        }
        if (!StringUtils.isEmpty(isHot)) {
            criteria1.andIsHotEqualTo(isHot);
            criteria2.andIsHotEqualTo(isHot);
        }if (!StringUtils.isEmpty(isNew)) {
            criteria1.andIsNewEqualTo(isNew);
            criteria2.andIsNewEqualTo(isNew);
        }

        criteria1.andStatusEqualTo(1);
        criteria2.andStatusEqualTo(1);

        List<PmsGoods> pmsGoods = goodsMapper.selectByExample(example);
        List<Long> categoryIdList = new ArrayList<>();
        for (PmsGoods  good: pmsGoods) {
            categoryIdList.add(good.getCategoryId());
        }
        return categoryIdList;
    }


}
