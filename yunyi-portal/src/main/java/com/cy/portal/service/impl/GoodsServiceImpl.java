package com.cy.portal.service.impl;

import com.cy.portal.service.GoodsService;
import com.cy.portal.vo.GoodsSpecificationVo;
import com.cy.yunyi.mapper.*;
import com.cy.yunyi.model.*;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    public List<PmsGoods> querySelective(String keyword, Long categoryId, Long brandId,
                                         Integer isHot, Integer isNew,
                                         Integer pageNum, Integer pageSize,
                                         String sort, String order) {
        PageHelper.startPage(pageNum,pageSize);
        PmsGoodsExample example = new PmsGoodsExample();
        PmsGoodsExample.Criteria criteria1 = example.or();
        PmsGoodsExample.Criteria criteria2 = example.or();
        if (!StringUtils.isEmpty(keyword)) {
            criteria1.andKeywordsLike("%" + keyword + "%");
            criteria2.andNameLike("%" + keyword + "%");
        }
        if (!StringUtils.isEmpty(categoryId) && categoryId != 0) {
            criteria1.andCategoryIdEqualTo(categoryId);
            criteria2.andCategoryIdEqualTo(categoryId);
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

        //排序
        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        List<PmsGoods> goodsList = goodsMapper.selectByExample(example);
        return goodsList;
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

    @Override
    public List<PmsGoods> recommendByCategoryId(Long itemId, Long categoryId) {
        PmsGoodsExample example = new PmsGoodsExample();
        example.createCriteria().andIdNotEqualTo(itemId).andCategoryIdEqualTo(categoryId).andIsHotEqualTo(1).andStatusEqualTo(1);
        List<PmsGoods> goodsList = goodsMapper.selectByExample(example);
        return goodsList;
    }

}
