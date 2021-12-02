package com.cy.yunyi.admin.service.impl;

import com.cy.yunyi.admin.service.PmsCategoryService;
import com.cy.yunyi.mapper.PmsCategoryMapper;
import com.cy.yunyi.model.PmsCategory;
import com.cy.yunyi.model.PmsCategoryExample;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author: chx
 * @Description: 商品分类管理Service实现类
 * @DateTime: 2021/11/30 16:37
 **/
@Service
public class PmsCategoryServiceImpl implements PmsCategoryService {
    
    @Autowired
    private PmsCategoryMapper categoryMapper;

    @Override
    public int create(PmsCategory category) {
        if (null == category.getSortOrder()){
            category.setSortOrder(0);
        }
        category.setCreateTime(new Date());
        category.setStatus(1);
        int count = categoryMapper.insert(category);
        return count;
    }

    @Override
    public int update(Long id, PmsCategory category) {
        category.setId(id);
        category.setUpdateTime(new Date());
        int count = categoryMapper.updateByPrimaryKey(category);
        return count;
    }

    @Override
    public List<PmsCategory> list(String keyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        PageHelper.orderBy("sort_order asc");
        PmsCategoryExample example = new PmsCategoryExample();
        example.createCriteria().andNameEqualTo("%" + keyword + "%");
        List<PmsCategory> categoryList = categoryMapper.selectByExample(example);
        return categoryList;
    }

    @Override
    public int delete(Long id) {
        int count = categoryMapper.deleteByPrimaryKey(id);
        return count;
    }
}
