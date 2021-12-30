package com.cy.portal.service.impl;

import com.cy.portal.service.HomeService;
import com.cy.portal.vo.HomeContentVo;
import com.cy.yunyi.mapper.PmsBrandMapper;
import com.cy.yunyi.mapper.PmsCategoryMapper;
import com.cy.yunyi.mapper.PmsGoodsMapper;
import com.cy.yunyi.mapper.SmsHomeAdvertiseMapper;
import com.cy.yunyi.model.*;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: chx
 * @Description: 首页内容管理Service实现类
 * @DateTime: 2021/12/13 23:48
 **/
@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private SmsHomeAdvertiseMapper advertiseMapper;
    @Autowired
    private PmsCategoryMapper categoryMapper;
    @Autowired
    private PmsBrandMapper brandMapper;
    @Autowired
    private PmsGoodsMapper goodsMapper;

    @Override
    public HomeContentVo content() {
        HomeContentVo result = new HomeContentVo();
        //获取首页广告
        result.setAdvertiseList(this.getHomeAdvertiseList(5));
        //获取分类列表
        result.setCategoryList(this.getCategoryList(10));
        //获取品牌列表
        result.setBrandList(this.getBrandList(4));
        //获取新品推荐
        result.setNewGoodsList(this.getNewGoodsList(6));
        //获取人气推荐
        result.setHotGoodsList(this.getHotGoodsList(6));
        //获取商品总数
        result.setGoodsCount(this.getGoodsCount());
        return result;
    }

    private List<SmsHomeAdvertise> getHomeAdvertiseList(int size) {
        PageHelper.startPage(1,size);
        SmsHomeAdvertiseExample example = new SmsHomeAdvertiseExample();
        example.createCriteria().andTypeEqualTo(1).andStatusEqualTo(1);
        example.setOrderByClause("sort desc");
        return advertiseMapper.selectByExample(example);
    }

    private List<PmsCategory> getCategoryList(int size) {
        PageHelper.startPage(1,size);
        PmsCategoryExample example = new PmsCategoryExample();
        example.createCriteria().andLevelEqualTo("L1").andStatusEqualTo(1);;
        example.setOrderByClause("sort_order desc");
        return categoryMapper.selectByExample(example);
    }

    private List<PmsBrand> getBrandList(int size) {
        PageHelper.startPage(1,size);
        PmsBrandExample example = new PmsBrandExample();
        example.createCriteria().andStatusEqualTo(1);
        example.setOrderByClause("sort_order desc");
        return brandMapper.selectByExample(example);
    }

    private List<PmsGoods> getNewGoodsList(int size) {
        PageHelper.startPage(1,size);
        PmsGoodsExample example = new PmsGoodsExample();
        example.createCriteria().andIsNewEqualTo(1).andStatusEqualTo(1);;
        example.setOrderByClause("sort_order desc");
        return goodsMapper.selectByExample(example);
    }

    private List<PmsGoods> getHotGoodsList(int size) {
        PageHelper.startPage(1,size);
        PmsGoodsExample example = new PmsGoodsExample();
        example.createCriteria().andIsHotEqualTo(1).andStatusEqualTo(1);;
        example.setOrderByClause("sort_order desc");
        return goodsMapper.selectByExample(example);
    }

    private Long getGoodsCount() {
        PmsGoodsExample example = new PmsGoodsExample();
        example.createCriteria().andStatusEqualTo(1);
        return goodsMapper.countByExample(example);
    }
}
