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
        Date createTime = new Date();
        Integer status = goods.getStatus();

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
        goods.setCreateTime(createTime);

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
            specification.setCreateTime(createTime);
            specification.setStatus(status);
            specificationMapper.insert(specification);
        }

        // 商品参数表pms_goods_attribute
        for (PmsGoodsAttribute attribute : attributes) {
            attribute.setGoodsId(goods.getId());
            attribute.setCreateTime(createTime);
            attribute.setStatus(status);
            attributeMapper.insert(attribute);
        }

        // 商品货品表pms_product
        for (PmsGoodsProduct product : products) {
            product.setGoodsId(goods.getId());
            product.setCreateTime(createTime);
            product.setStatus(status);
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
        Date createTime = goods.getCreateTime();
        Integer status = goods.getStatus();
        Date updateTime = new Date();
        goods.setRetailPrice(retailPrice);
        goods.setUpdateTime(updateTime);

        // 商品基本信息表pms_goods
        if (goodsMapper.updateByPrimaryKeySelective(goods) == 0) {
            throw new RuntimeException("更新数据失败");
        }

        //删除原有数据
        PmsGoodsSpecificationExample pmsGoodsSpecificationExample = new PmsGoodsSpecificationExample();
        pmsGoodsSpecificationExample.createCriteria().andGoodsIdEqualTo(id);
        specificationMapper.deleteByExample(pmsGoodsSpecificationExample);

        PmsGoodsAttributeExample pmsGoodsAttributeExample = new PmsGoodsAttributeExample();
        pmsGoodsAttributeExample.createCriteria().andGoodsIdEqualTo(id);
        attributeMapper.deleteByExample(pmsGoodsAttributeExample);

        PmsGoodsProductExample pmsGoodsProductExample = new PmsGoodsProductExample();
        pmsGoodsProductExample.createCriteria().andGoodsIdEqualTo(id);
        productMapper.deleteByExample(pmsGoodsProductExample);



        // 商品规格表pms_goods_specification
        for (PmsGoodsSpecification specification : specifications) {
            specification.setGoodsId(goods.getId());
            specification.setCreateTime(createTime);
            specification.setUpdateTime(updateTime);
            specification.setStatus(status);
            specificationMapper.insert(specification);
        }

        // 商品参数表pms_goods_attribute
        for (PmsGoodsAttribute attribute : attributes) {
            attribute.setGoodsId(goods.getId());
            attribute.setCreateTime(createTime);
            attribute.setUpdateTime(updateTime);
            attribute.setStatus(status);
            attributeMapper.insert(attribute);
        }

        // 商品货品表pms_product
        for (PmsGoodsProduct product : products) {
            product.setGoodsId(goods.getId());
            product.setCreateTime(createTime);
            product.setUpdateTime(updateTime);
            product.setStatus(status);
            productMapper.insert(product);
        }
        return CommonResult.success(goodsAllParam);
    }

    @Override
    public int update(Long id, PmsGoods goods) {
        goods.setId(id);
        goods.setUpdateTime(new Date());
        int count = goodsMapper.updateByPrimaryKeySelective(goods);
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

            String[] specificationsArr = product.getSpecifications();
            if (specificationsArr.length == 0) {
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
        if (!StringUtils.isEmpty(keyword)){
            example.createCriteria().andNameLike("%" + keyword + "%");
        }
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

    @Override
    public PmsGoodsAllParam updateInfo(Long id) {
        PmsGoodsAllParam pmsGoodsAllParam = new PmsGoodsAllParam();
        PmsGoods pmsGoods = goodsMapper.selectByPrimaryKey(id);

        PmsGoodsSpecificationExample pmsGoodsSpecificationExample = new PmsGoodsSpecificationExample();
        pmsGoodsSpecificationExample.createCriteria().andGoodsIdEqualTo(id);
        List<PmsGoodsSpecification> goodsSpecificationList = specificationMapper.selectByExample(pmsGoodsSpecificationExample);

        PmsGoodsAttributeExample pmsGoodsAttributeExample = new PmsGoodsAttributeExample();
        pmsGoodsAttributeExample.createCriteria().andGoodsIdEqualTo(id);
        List<PmsGoodsAttribute> goodsAttributeList = attributeMapper.selectByExample(pmsGoodsAttributeExample);

        PmsGoodsProductExample pmsGoodsProductExample = new PmsGoodsProductExample();
        pmsGoodsProductExample.createCriteria().andGoodsIdEqualTo(id);
        List<PmsGoodsProduct> goodsProductList = productMapper.selectByExample(pmsGoodsProductExample);

        pmsGoodsAllParam.setGoods(pmsGoods);
        pmsGoodsAllParam.setAttributes(goodsAttributeList.toArray(new PmsGoodsAttribute[goodsAttributeList.size()]));
        pmsGoodsAllParam.setProducts(goodsProductList.toArray(new PmsGoodsProduct[goodsProductList.size()]));
        pmsGoodsAllParam.setSpecifications(goodsSpecificationList.toArray(new PmsGoodsSpecification[goodsSpecificationList.size()]));
        return pmsGoodsAllParam;
    }

}
