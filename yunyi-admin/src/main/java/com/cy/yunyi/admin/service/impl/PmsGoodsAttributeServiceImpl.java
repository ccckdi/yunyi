package com.cy.yunyi.admin.service.impl;
import com.cy.yunyi.admin.service.PmsGoodsAttributeService;
import com.cy.yunyi.mapper.PmsGoodsAttributeMapper;
import com.cy.yunyi.model.PmsGoodsAttribute;
import com.cy.yunyi.model.PmsGoodsAttributeExample;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author: chx
 * @Description: 商品参数管理Service实现类
 * @DateTime: 2021/12/1 10:35
 **/
@Service
public class PmsGoodsAttributeServiceImpl implements PmsGoodsAttributeService {
    @Autowired
    private PmsGoodsAttributeMapper attributeMapper;

    @Override
    public int create(PmsGoodsAttribute attribute) {
        attribute.setCreateTime(new Date());
        attribute.setStatus(1);
        int count = attributeMapper.insert(attribute);
        return count;
    }

    @Override
    public int update(Long id, PmsGoodsAttribute attribute) {
        attribute.setId(id);
        attribute.setUpdateTime(new Date());
        int count = attributeMapper.updateByPrimaryKey(attribute);
        return count;
    }

    @Override
    public List<PmsGoodsAttribute> list(String keyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        PmsGoodsAttributeExample example = new PmsGoodsAttributeExample();
        //example.createCriteria().andAttributeLike("%" + keyword + "%");
        List<PmsGoodsAttribute> attributeList = attributeMapper.selectByExample(example);
        return attributeList;
    }

    @Override
    public List<PmsGoodsAttribute> listByGoodId(Long goodsId) {
        PmsGoodsAttributeExample example = new PmsGoodsAttributeExample();
        example.createCriteria().andGoodsIdEqualTo(goodsId);
        List<PmsGoodsAttribute> attributeList = attributeMapper.selectByExample(example);
        return attributeList;
    }

    @Override
    public int delete(Long id) {
        int count = attributeMapper.deleteByPrimaryKey(id);
        return count;
    }
}
