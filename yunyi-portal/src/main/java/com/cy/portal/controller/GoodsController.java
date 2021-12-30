package com.cy.portal.controller;

import com.cy.portal.service.CategoryService;
import com.cy.portal.service.GoodsService;
import com.cy.portal.service.IssueService;
import com.cy.portal.vo.CategoryInfoVo;
import com.cy.portal.vo.GoodsInfoVo;
import com.cy.portal.vo.GoodsSpecificationVo;
import com.cy.yunyi.common.api.CommonResult;
import com.cy.yunyi.model.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public CommonResult listByCategoryId(@RequestParam(value = "categoryId") Long categoryId,
                                         @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        List<PmsGoods> goodsList = goodsService.getGoodsListByCategoryId(categoryId,pageNum,pageSize);
        return CommonResult.success(goodsList);
    }

    /**
     * 根据id获取商品详情
     */
    @ApiOperation("分页内容页信息展示")
    @GetMapping("/detail")
    public CommonResult detail(@RequestParam Long id){
        GoodsInfoVo goodsInfoVo = new GoodsInfoVo();

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
