package com.cy.yunyi.admin.service.impl;
import com.cy.yunyi.admin.service.PmsGoodsProductService;
import com.cy.yunyi.mapper.PmsGoodsProductMapper;
import com.cy.yunyi.model.PmsGoodsProduct;
import com.cy.yunyi.model.PmsGoodsProductExample;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author: chx
 * @Description: 商品库存管理Service实现类
 * @DateTime: 2021/12/1 10:35
 **/
@Service
public class PmsGoodsProductServiceImpl implements PmsGoodsProductService {
    @Autowired
    private PmsGoodsProductMapper productMapper;

    @Override
    public int create(PmsGoodsProduct product) {
        product.setCreateTime(new Date());
        product.setStatus(1);
        int count = productMapper.insert(product);
        return count;
    }

    @Override
    public int update(Long id, PmsGoodsProduct product) {
        product.setId(id);
        product.setUpdateTime(new Date());
        int count = productMapper.updateByPrimaryKey(product);
        return count;
    }

    @Override
    public List<PmsGoodsProduct> list(String keyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        PmsGoodsProductExample example = new PmsGoodsProductExample();
        //example.createCriteria().andGoodsIdEqualTo(Integer.valueOf(keyword));
        List<PmsGoodsProduct> productList = productMapper.selectByExample(example);
        return productList;
    }

    @Override
    public List<PmsGoodsProduct> listByGoodId(Long goodsId) {
        PmsGoodsProductExample example = new PmsGoodsProductExample();
        example.createCriteria().andGoodsIdEqualTo(goodsId);
        List<PmsGoodsProduct> productList = productMapper.selectByExample(example);
        return productList;
    }

    @Override
    public int delete(Long id) {
        int count = productMapper.deleteByPrimaryKey(id);
        return count;
    }
}
