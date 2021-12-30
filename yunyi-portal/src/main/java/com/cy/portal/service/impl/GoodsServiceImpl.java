package com.cy.portal.service.impl;

import com.cy.portal.service.GoodsService;
import com.cy.portal.vo.GoodsSpecificationVo;
import com.cy.yunyi.mapper.*;
import com.cy.yunyi.model.*;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: chx
 * @Description: 商品信息Service实现类
 * @DateTime: 2021/12/16 10:25
 **/
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private PmsGoodsMapper goodsMapper;

    @Autowired
    private PmsGoodsAttributeMapper attributeMapper;


    @Autowired
    private PmsGoodsSpecificationMapper specificationMapper;

    @Autowired
    private PmsBrandMapper brandMapper;

    @Autowired
    private PmsGoodsProductMapper productMapper;

    @Override
    public Long getGoodsCount() {
        return goodsMapper.countByExample(new PmsGoodsExample());
    }

    @Override
    public List<PmsGoods> getGoodsListByCategoryId(Long id, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        PmsGoodsExample example = new PmsGoodsExample();
        example.createCriteria().andCategoryIdEqualTo(id).andStatusEqualTo(1);
        List<PmsGoods> goodsList = goodsMapper.selectByExample(example);
        return goodsList;
    }

    @Override
    public PmsGoods getById(Long id) {
        return goodsMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<PmsGoodsAttribute> getAttributeById(Long id) {
        PmsGoodsAttributeExample example = new PmsGoodsAttributeExample();
        example.createCriteria().andGoodsIdEqualTo(id).andStatusEqualTo(1);
        List<PmsGoodsAttribute> attributeList = attributeMapper.selectByExample(example);
        return attributeList;
    }

    @Override
    public List<GoodsSpecificationVo> getSpecificationById(Long id) {
        PmsGoodsSpecificationExample example = new PmsGoodsSpecificationExample();
        example.createCriteria().andGoodsIdEqualTo(id).andStatusEqualTo(1);
        List<PmsGoodsSpecification> goodsSpecificationList = specificationMapper.selectByExample(example);

        Map<String, GoodsSpecificationVo> map = new HashMap<>();
        List<GoodsSpecificationVo> specificationVoList = new ArrayList<>();

        for (PmsGoodsSpecification goodsSpecification : goodsSpecificationList) {
            String specification = goodsSpecification.getSpecification();
            GoodsSpecificationVo goodsSpecificationVo = map.get(specification);
            if (goodsSpecificationVo == null) {
                goodsSpecificationVo = new GoodsSpecificationVo();
                goodsSpecificationVo.setName(specification);
                List<PmsGoodsSpecification> valueList = new ArrayList<>();
                valueList.add(goodsSpecification);
                goodsSpecificationVo.setValueList(valueList);
                map.put(specification, goodsSpecificationVo);
                specificationVoList.add(goodsSpecificationVo);
            } else {
                List<PmsGoodsSpecification> valueList = goodsSpecificationVo.getValueList();
                valueList.add(goodsSpecification);
            }
        }

        return specificationVoList;
    }

    @Override
    public PmsBrand getBrandById(Long id) {
        PmsGoods goods = goodsMapper.selectByPrimaryKey(id);
        Long brandId = goods.getBrandId();
        PmsBrandExample example = new PmsBrandExample();
        example.createCriteria().andIdEqualTo(brandId).andStatusEqualTo(1);
        List<PmsBrand> brandList = brandMapper.selectByExample(example);
        if (brandList != null && brandList.size() > 0){
            return brandList.get(0);
        }
        return null;
    }

    @Override
    public List<PmsGoodsProduct> getProductById(Long id) {
        PmsGoodsProductExample example = new PmsGoodsProductExample();
        example.createCriteria().andGoodsIdEqualTo(id).andStatusEqualTo(1);
        List<PmsGoodsProduct> productList = productMapper.selectByExample(example);
        return productList;
    }
}
