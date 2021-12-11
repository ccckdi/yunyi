package com.cy.yunyi.admin.service.impl;

import com.cy.yunyi.admin.dto.UmsMenuNode;
import com.cy.yunyi.admin.service.PmsCategoryService;
import com.cy.yunyi.admin.vo.PmsCategoryVo;
import com.cy.yunyi.mapper.PmsCategoryMapper;
import com.cy.yunyi.model.PmsCategory;
import com.cy.yunyi.model.PmsCategoryExample;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

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
        if (category.getPid() == 0){
            category.setLevel("L1");
        }else {
            category.setLevel("L2");
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
        if (category.getPid() != null && category.getPid() == 0){
            category.setLevel("L1");
        }else if (category.getPid() != null){
            category.setLevel("L2");
        }
        int count = categoryMapper.updateByPrimaryKeySelective(category);
        return count;
    }

    @Override
    public List<PmsCategory> list(Long parentId,Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);

        PmsCategoryExample example = new PmsCategoryExample();
        example.setOrderByClause("sort_order asc");
        example.createCriteria().andPidEqualTo(parentId);
        List<PmsCategory> categoryList = categoryMapper.selectByExample(example);
//        List<PmsCategoryVo> categoryVoList = categoryList.stream()
//                .filter(category -> category.getPid().equals(0L))
//                .map(category -> covertCategoryNode(category, categoryList)).collect(Collectors.toList());
      return categoryList;
    }


    @Override
    public int delete(Long id) {
        int count = categoryMapper.deleteByPrimaryKey(id);
        return count;
    }

    @Override
    public PmsCategory info(Long id) {
        PmsCategory category = categoryMapper.selectByPrimaryKey(id);
        return category;
    }

    @Override
    public List<PmsCategoryVo> listWithChildren() {
        PmsCategoryExample example = new PmsCategoryExample();
        List<PmsCategory> categoryList = categoryMapper.selectByExample(example);
        List<PmsCategoryVo> categoryVoList = categoryList.stream()
                .filter(category -> category.getPid().equals(0L))
                .map(category -> covertCategoryNode(category, categoryList)).collect(Collectors.toList());
        return categoryVoList;
    }

    /***
     * 树状结构数据处理
     */
    private PmsCategoryVo covertCategoryNode(PmsCategory category, List<PmsCategory> categoryList) {
        PmsCategoryVo categoryVo = new PmsCategoryVo();
        BeanUtils.copyProperties(category, categoryVo);
        List<PmsCategoryVo> children = categoryList.stream()
                .filter(subCategory -> subCategory.getPid().equals(category.getId()))
                .map(subMenu -> covertCategoryNode(subMenu, categoryList)).collect(Collectors.toList());
        categoryVo.setChildren(children);
        return categoryVo;
    }
}
