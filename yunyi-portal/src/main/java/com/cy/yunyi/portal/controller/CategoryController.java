package com.cy.yunyi.portal.controller;

import com.cy.yunyi.portal.service.CategoryService;
import com.cy.yunyi.portal.vo.CategoryContentVo;
import com.cy.yunyi.common.api.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



/**
 * @Author: chx
 * @Description: 分类管理Controller
 * @DateTime: 2021/12/15 23:16
 **/
@RestController
@Api(tags = "CategoryController", description = "分类管理")
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @ApiOperation("分页内容页信息展示")
    @GetMapping("/index")
    public CommonResult<CategoryContentVo> index() {
        CategoryContentVo contentResult = categoryService.content();
        return CommonResult.success(contentResult);
    }

    @ApiOperation("根据id获取当前分类信息")
    @GetMapping("/current")
    public CommonResult<CategoryContentVo> getCurrentCategoryById(@RequestParam Long id) {
        CategoryContentVo contentResult = new CategoryContentVo();
        contentResult.setCurrentCategory(categoryService.getCategoryById(id));
        contentResult.setCurrentSubCategoryList(categoryService.getSubCategoryById(id));
        return CommonResult.success(contentResult);
    }

}
