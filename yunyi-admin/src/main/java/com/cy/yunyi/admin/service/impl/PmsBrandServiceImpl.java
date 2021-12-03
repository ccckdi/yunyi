package com.cy.yunyi.admin.service.impl;

import com.cy.yunyi.admin.service.PmsBrandService;
import com.cy.yunyi.mapper.PmsBrandMapper;
import com.cy.yunyi.model.PmsBrand;
import com.cy.yunyi.model.PmsBrandExample;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @Author: chx
 * @Description: 商品品牌管理Service实现类
 * @DateTime: 2021/11/30 11:01
 **/
@Service
public class PmsBrandServiceImpl implements PmsBrandService {

    @Autowired
    private PmsBrandMapper brandMapper;

    @Override
    public int create(PmsBrand brand) {
        if (null == brand.getSortOrder()){
            brand.setSortOrder(0);
        }
        brand.setCreateTime(new Date());
        brand.setStatus(1);
        int count = brandMapper.insert(brand);
        return count;
    }

    @Override
    public int update(Long id, PmsBrand brand) {
        brand.setId(id);
        brand.setUpdateTime(new Date());
        int count = brandMapper.updateByPrimaryKey(brand);
        return count;
    }

    @Override
    public List<PmsBrand> list(String keyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        PageHelper.orderBy("sort_order asc");
        PmsBrandExample example = new PmsBrandExample();
        if (!StringUtils.isEmpty(keyword)){
            example.createCriteria().andNameLike("%" + keyword + "%");
        }
        List<PmsBrand> brandList = brandMapper.selectByExample(example);
        return brandList;
    }

    @Override
    public int delete(Long id) {
        int count = brandMapper.deleteByPrimaryKey(id);
        return count;
    }
}
