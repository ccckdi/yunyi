package com.cy.yunyi.admin.service.impl;
import com.cy.yunyi.admin.service.PmsGoodsSpecificationService;
import com.cy.yunyi.mapper.PmsGoodsSpecificationMapper;
import com.cy.yunyi.model.PmsGoodsSpecification;
import com.cy.yunyi.model.PmsGoodsSpecificationExample;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author: chx
 * @Description: 商品规格管理Service实现类
 * @DateTime: 2021/12/1 10:35
 **/
@Service
public class PmsGoodsSpecificationServiceImpl implements PmsGoodsSpecificationService {
    @Autowired
    private PmsGoodsSpecificationMapper specificationMapper;

    @Override
    public int create(PmsGoodsSpecification specification) {
        specification.setCreateTime(new Date());
        specification.setStatus(1);
        int count = specificationMapper.insert(specification);
        return count;
    }

    @Override
    public int update(Long id, PmsGoodsSpecification specification) {
        specification.setId(id);
        specification.setUpdateTime(new Date());
        int count = specificationMapper.updateByPrimaryKey(specification);
        return count;
    }

    @Override
    public List<PmsGoodsSpecification> list(String keyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        PmsGoodsSpecificationExample example = new PmsGoodsSpecificationExample();
        //example.createCriteria().andGoodsIdEqualTo(Integer.valueOf(keyword));
        List<PmsGoodsSpecification> specificationList = specificationMapper.selectByExample(example);
        return specificationList;
    }

    @Override
    public List<PmsGoodsSpecification> listByGoodId(Long goodsId) {
        PmsGoodsSpecificationExample example = new PmsGoodsSpecificationExample();
        example.createCriteria().andGoodsIdEqualTo(goodsId);
        List<PmsGoodsSpecification> specificationList = specificationMapper.selectByExample(example);
        return specificationList;
    }

    @Override
    public int delete(Long id) {
        int count = specificationMapper.deleteByPrimaryKey(id);
        return count;
    }
}
