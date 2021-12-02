package com.cy.yunyi.admin.service.impl;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSONArray;
import com.cy.yunyi.admin.dto.PmsGoodsAllParam;
import com.cy.yunyi.admin.service.*;
import com.cy.yunyi.common.api.CommonResult;
import com.cy.yunyi.common.api.ResultCode;
import com.cy.yunyi.mapper.*;
import com.cy.yunyi.model.*;
import com.github.pagehelper.PageHelper;
import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author: chx
 * @Description: 商品管理Service实现类
 * TODO 参数校验
 * @DateTime: 2021/11/30 16:37
 **/
@Service
public class PmsGoodsServiceImpl implements PmsGoodsService {
    
    @Autowired
    private PmsGoodsMapper goodsMapper;
    @Autowired
    private PmsGoodsSpecificationMapper specificationMapper;
    @Autowired
    private PmsGoodsAttributeMapper attributeMapper;
    @Autowired
    private PmsGoodsProductMapper productMapper;
    @Autowired
    private PmsCategoryMapper categoryMapper;
    @Autowired
    private PmsBrandMapper brandMapper;
//    @Autowired
//    private PmsCartMapper cartMapper;

    @Override
    public CommonResult create(PmsGoodsAllParam goodsAllParam) {
        CommonResult error = validate(goodsAllParam);
        if (error != null) {
            return error;
        }

        PmsGoods goods = goodsAllParam.getGoods();
        PmsGoodsAttribute[] attributes = goodsAllParam.getAttributes();
        PmsGoodsSpecification[] specifications = goodsAllParam.getSpecifications();
        PmsGoodsProduct[] products = goodsAllParam.getProducts();

        String name = goods.getName();
        if (listByName(name).size() > 0) {
            return CommonResult.failed(ResultCode.FAILED, "商品名已经存在");
        }

        // 商品表里面有一个字段retailPrice记录当前商品的最低价
        BigDecimal retailPrice = new BigDecimal(Integer.MAX_VALUE);
        for (PmsGoodsProduct product : products) {
            BigDecimal productPrice = product.getPrice();
            if (retailPrice.compareTo(productPrice) == 1) {
                retailPrice = productPrice;
            }
        }
        goods.setRetailPrice(retailPrice);

        // 商品基本信息表pms_goods
        goodsMapper.insert(goods);

        //将生成的分享图片地址写入数据库
        //String url = qCodeMapper.createGoodShareImage(goods.getId().toString(), goods.getPicUrl(), goods.getName());
//        if (!StringUtils.isEmpty(url)) {
//            goods.setShareUrl(url);
//            if (goodsMapper.updateById(goods) == 0) {
//                throw new RuntimeException("更新数据失败");
//            }
//        }

        // 商品规格表pms_goods_specification
        for (PmsGoodsSpecification specification : specifications) {
            specification.setGoodsId(goods.getId());
            specificationMapper.insert(specification);
        }

        // 商品参数表pms_goods_attribute
        for (PmsGoodsAttribute attribute : attributes) {
            attribute.setGoodsId(goods.getId());
            attributeMapper.insert(attribute);
        }

        // 商品货品表pms_product
        for (PmsGoodsProduct product : products) {
            product.setGoodsId(goods.getId());
            productMapper.insert(product);
        }
        return CommonResult.success(goodsAllParam);
    }

    @Override
    public CommonResult update(Long id, PmsGoodsAllParam goodsAllParam) {
        CommonResult error = validate(goodsAllParam);
        if (error != null) {
            return error;
        }

        PmsGoods goods = goodsAllParam.getGoods();
        goods.setId(id);
        PmsGoodsAttribute[] attributes = goodsAllParam.getAttributes();
        PmsGoodsSpecification[] specifications = goodsAllParam.getSpecifications();
        PmsGoodsProduct[] products = goodsAllParam.getProducts();

        // 商品表里面有一个字段retailPrice记录当前商品的最低价
        BigDecimal retailPrice = new BigDecimal(Integer.MAX_VALUE);
        for (PmsGoodsProduct product : products) {
            BigDecimal productPrice = product.getPrice();
            if (retailPrice.compareTo(productPrice) == 1) {
                retailPrice = productPrice;
            }
        }
        goods.setRetailPrice(retailPrice);

        // 商品基本信息表pms_goods
        goodsMapper.insert(goods);
        if (goodsMapper.updateByPrimaryKey(goods) == 0) {
            throw new RuntimeException("更新数据失败");
        }

        //将生成的分享图片地址写入数据库
        //String url = qCodeMapper.createGoodShareImage(goods.getId().toString(), goods.getPicUrl(), goods.getName());
//        if (!StringUtils.isEmpty(url)) {
//            goods.setShareUrl(url);
//            if (goodsMapper.updateById(goods) == 0) {
//                throw new RuntimeException("更新数据失败");
//            }
//        }

        // 商品规格表pms_goods_specification
        for (PmsGoodsSpecification specification : specifications) {
            // 目前只支持更新规格表的图片字段
            if(specification.getUpdateTime() == null){
                specification.setSpecification(null);
                specification.setValue(null);
                specificationMapper.updateByPrimaryKey(specification);
            }
        }

        // 商品货品表pms_product
        for (PmsGoodsProduct product : products) {
            if(product.getUpdateTime() == null) {
                productMapper.updateByPrimaryKey(product);
            }
        }

        // 商品参数表pms_goods_attribute
        for (PmsGoodsAttribute attribute : attributes) {
            if (attribute.getId() == null || attribute.getId().equals(0)){
                attribute.setGoodsId(goods.getId());
                attributeMapper.insert(attribute);
            }
            else if(attribute.getStatus() == 0){
                attributeMapper.deleteByPrimaryKey(attribute.getId());
            }
            else if(attribute.getUpdateTime() == null){
                attributeMapper.updateByPrimaryKey(attribute);
            }
        }

        // 这里需要注意的是购物车pms_cart有些字段是拷贝商品的一些字段，因此需要及时更新
        // 目前这些字段是goods_sn, goods_name, price, pic_url
//        for (PmsGoodsProduct product : products) {
//            cartService.updateProduct(product.getId(), goods.getGoodsSn(), goods.getName(), product.getPrice(), product.getUrl());
//        }
        
        return CommonResult.success(goodsAllParam);
    }

    @Override
    public int update(Long id, PmsGoods goods) {
        goods.setId(id);
        goods.setUpdateTime(new Date());
        int count = goodsMapper.updateByPrimaryKey(goods);
        return count;
    }

    /***
     * 参数校验
     * @param goodsAllParam
     * @return
     */
    private CommonResult validate(PmsGoodsAllParam goodsAllParam) {
        PmsGoods goods = goodsAllParam.getGoods();
        String name = goods.getName();
        if (StringUtils.isEmpty(name)) {
            return CommonResult.validateFailed();
        }
        String goodsSn = goods.getGoodsSn();
        if (StringUtils.isEmpty(goodsSn)) {
            return CommonResult.validateFailed();
        }
        // 品牌商可以不设置，如果设置则需要验证品牌商存在
        Long brandId = goods.getBrandId();
        if (brandId != null && brandId != 0) {
            if (brandMapper.selectByPrimaryKey(brandId) == null) {
                return CommonResult.validateFailed();
            }
        }
        // 分类可以不设置，如果设置则需要验证分类存在
        Long categoryId = goods.getCategoryId();
        if (categoryId != null && categoryId != 0) {
            if (categoryMapper.selectByPrimaryKey(categoryId) == null) {
                return CommonResult.validateFailed();
            }
        }

        PmsGoodsAttribute[] attributes = goodsAllParam.getAttributes();
        for (PmsGoodsAttribute attribute : attributes) {
            String attr = attribute.getAttribute();
            if (StringUtils.isEmpty(attr)) {
                return CommonResult.validateFailed();
            }
            String value = attribute.getValue();
            if (StringUtils.isEmpty(value)) {
                return CommonResult.validateFailed();
            }
        }

        PmsGoodsSpecification[] specifications = goodsAllParam.getSpecifications();
        for (PmsGoodsSpecification specification : specifications) {
            String spec = specification.getSpecification();
            if (StringUtils.isEmpty(spec)) {
                return CommonResult.validateFailed();
            }
            String value = specification.getValue();
            if (StringUtils.isEmpty(value)) {
                return CommonResult.validateFailed();
            }
        }

        PmsGoodsProduct[] products = goodsAllParam.getProducts();
        for (PmsGoodsProduct product : products) {
            Integer number = product.getNumber();
            if (number == null || number < 0) {
                return CommonResult.validateFailed();
            }

            BigDecimal price = product.getPrice();
            if (price == null) {
                return CommonResult.validateFailed();
            }

            List<String> productSpecifications = JSONArray.parseArray(product.getSpecifications(), String.class);
            if (productSpecifications.size() == 0) {
                return CommonResult.validateFailed();
            }
        }

        return null;
    }

    @Override
    public List<PmsGoods> list(String keyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        PageHelper.orderBy("sort_order asc");
        PmsGoodsExample example = new PmsGoodsExample();
        example.createCriteria().andNameLike("%" + keyword + "%");
        List<PmsGoods> goodsList = goodsMapper.selectByExample(example);
        return goodsList;
    }

    @Override
    public List<PmsGoods> listByName(String name) {
        PmsGoodsExample example = new PmsGoodsExample();
        example.createCriteria().andNameEqualTo(name);
        List<PmsGoods> goodsList = goodsMapper.selectByExample(example);
        return goodsList;
    }

    @Override
    public int delete(Long id) {
        int count = goodsMapper.deleteByPrimaryKey(id);

        PmsGoodsSpecificationExample specificationExample = new PmsGoodsSpecificationExample();
        specificationExample.createCriteria().andGoodsIdEqualTo(id);
        specificationMapper.deleteByExample(specificationExample);

        PmsGoodsAttributeExample attributeExample = new PmsGoodsAttributeExample();
        attributeExample.createCriteria().andGoodsIdEqualTo(id);
        attributeMapper.deleteByExample(attributeExample);

        PmsGoodsProductExample productExample = new PmsGoodsProductExample();
        productExample.createCriteria().andGoodsIdEqualTo(id);
        productMapper.deleteByExample(productExample);

        return count;
    }

}
