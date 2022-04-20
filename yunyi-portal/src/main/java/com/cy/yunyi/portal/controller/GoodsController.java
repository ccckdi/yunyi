package com.cy.yunyi.portal.controller;

import com.cy.yunyi.portal.annotation.LoginUser;
import com.cy.yunyi.portal.controller.validator.Order;
import com.cy.yunyi.portal.controller.validator.Sort;
import com.cy.yunyi.portal.service.*;
import com.cy.yunyi.portal.vo.CategoryInfoVo;
import com.cy.yunyi.portal.vo.GoodsInfoVo;
import com.cy.yunyi.portal.vo.GoodsSpecificationVo;
import com.cy.yunyi.common.api.CommonResult;
import com.cy.yunyi.model.*;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @Author: chx
 * @Description: 商品信息Controller
 * @DateTime: 2021/12/16 9:53
 **/
@RestController
@Api(tags = "GoodsController", description = "商品信息管理")
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private IssueService issueService;

    @Autowired
    private FootprintService footprintService;

    @Autowired
    private SearchHistoryService searchHistoryService;

    /**
     * 获取商品总数
     */
    @ApiOperation("分页内容页信息展示")
    @GetMapping("/count")
    public CommonResult count() {
        Long goodsCount = goodsService.getGoodsCount();
        return CommonResult.success(goodsCount);
    }

    @ApiOperation("根据id获取当前父类下子类信息")
    @GetMapping("/category")
    public CommonResult<CategoryInfoVo> getCategoryInfoById(@RequestParam Long id) {
        CategoryInfoVo categoryInfoVo = new CategoryInfoVo();

        PmsCategory currentCategory = categoryService.getCategoryById(id);

        if ("L1".equals(currentCategory.getLevel())){
            categoryInfoVo.setParentCategory(currentCategory);
            List<PmsCategory> subCategoryList = categoryService.getSubCategoryById(id);
            categoryInfoVo.setBrotherCategory(subCategoryList);
            if (subCategoryList != null && subCategoryList.size() > 0){
                categoryInfoVo.setCurrentCategory(subCategoryList.get(0));
            }
        }else {
            //参数为子级id需要先查询父级id
            PmsCategory parCagetory = categoryService.getCategoryById(currentCategory.getPid());
            categoryInfoVo.setCurrentCategory(currentCategory);
            categoryInfoVo.setParentCategory(parCagetory);
            List<PmsCategory> subCategoryList = categoryService.getSubCategoryById(parCagetory.getId());
            categoryInfoVo.setBrotherCategory(subCategoryList);
        }

        return CommonResult.success(categoryInfoVo);
    }

    /**
     * 根据分类id获取商品列表
     */
    @ApiOperation("分页内容页信息展示")
    @GetMapping("/list")
    public CommonResult listByCategoryId(@LoginUser @RequestParam(required = false) Long userId,
                                         @RequestParam(value = "categoryId",required = false) Long categoryId,
                                         @RequestParam(value = "brandId",required = false) Long brandId,
                                         @RequestParam(value = "keyword",required = false) String keyword,
                                         @RequestParam(value = "isNew",required = false) Integer isNew,
                                         @RequestParam(value = "isHot",required = false) Integer isHot,
                                         @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                         @Sort(accepts = {"create_time", "retail_price", "name"}) @RequestParam(defaultValue = "create_time",required = false) String sort,
                                         @Order @RequestParam(defaultValue = "desc",required = false) String order) {
        //添加搜索历史
        if (userId != null && !StringUtils.isEmpty(keyword)) {
            RmsSearchHistory searchHistory = new RmsSearchHistory();
            searchHistory.setKeyword(keyword);
            searchHistory.setUserId(userId);
            searchHistory.setSource("wx");
            searchHistory.setCreateTime(new Date());
            searchHistory.setStatus(1);
            searchHistoryService.create(searchHistory);
        }

        //分页查询列表数据
        List<PmsGoods> goodsList = goodsService.querySelective(keyword,categoryId, brandId, isHot, isNew, pageNum,pageSize, sort, order);

        // 查询商品所属类目列表
        List<Long> categoryIdList = categoryService.queryIdsSelective(brandId, keyword, isHot, isNew);

        List<PmsCategory> categoryList = null;
        if (goodsList.size() != 0) {
            categoryList = categoryService.queryL2ByGoods(categoryIdList);
        } else {
            categoryList = new ArrayList<>(0);
        }

        PageInfo<PmsGoods> pagedList = PageInfo.of(goodsList);
        Map<String, Object> result = new HashMap<>();
        result.put("list", goodsList);
        result.put("total", pagedList.getTotal());
        result.put("page", pagedList.getPageNum());
        result.put("limit", pagedList.getPageSize());
        result.put("pages", pagedList.getPages());
        result.put("filterCategoryList", categoryList);

        return CommonResult.success(result);
    }

    /**
     * 根据id获取商品详情
     * 用户可以不登录。
     * 如果用户登录，则记录用户足迹以及返回用户收藏信息。
     */
    @ApiOperation("分页内容页信息展示")
    @GetMapping("/detail")
    public CommonResult detail(@LoginUser @RequestParam(required = false) Long userId, @RequestParam Long id){
        GoodsInfoVo goodsInfoVo = new GoodsInfoVo();

//        // 用户收藏
//        int userHasCollect = 0;
//        if (userId != null) {
//            userHasCollect = collectService.count(userId, (byte)0, id);
//        }
        if (userId != null) {
            //添加用户足迹
            RmsFootprint footprint = new RmsFootprint();
            footprint.setUserId(userId);
            footprint.setGoodsId(id);
            footprintService.create(footprint);
        }

        //商品信息
        PmsGoods info = goodsService.getById(id);

        //商品属性
        List<PmsGoodsAttribute> attributeList = goodsService.getAttributeById(id);

        //商品规格
        List<GoodsSpecificationVo> specificationList = goodsService.getSpecificationById(id);

        //商品品牌
        PmsBrand brand = goodsService.getBrandById(id);

        //商品产品
        List<PmsGoodsProduct>  productList = goodsService.getProductById(id);

        //常见问题
        List<BmsIssue> issueList = issueService.list();


        goodsInfoVo.setInfo(info);
        goodsInfoVo.setAttributeList(attributeList);
        goodsInfoVo.setSpecificationList(specificationList);
        goodsInfoVo.setBrand(brand);
        goodsInfoVo.setProductList(productList);
        goodsInfoVo.setIssueList(issueList);

        return CommonResult.success(goodsInfoVo);
    }
}
