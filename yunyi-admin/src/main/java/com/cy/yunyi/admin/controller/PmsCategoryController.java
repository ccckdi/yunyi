package com.cy.yunyi.admin.controller;

import com.cy.yunyi.admin.service.PmsCategoryService;
import com.cy.yunyi.common.api.CommonPage;
import com.cy.yunyi.common.api.CommonResult;
import com.cy.yunyi.model.PmsCategory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* @Author: chx
* @Description: 商品分类管理Controller
* @DateTime: 2021/11/30 16:35
**/
@RestController
@Api(tags = "PmsCategoryController", description = "商品分类管理")
@RequestMapping("/category")
public class PmsCategoryController {

    @Autowired
    private PmsCategoryService categoryService;

    @ApiOperation("添加分类")
    @PostMapping("/create")
    public CommonResult create(@RequestBody PmsCategory category) {
        int count = categoryService.create(category);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("修改分类")
    @PostMapping("/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody PmsCategory category) {
        int count = categoryService.update(id, category);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("根据分类名称分页获取分类列表")
    @GetMapping("list")
    public CommonResult<CommonPage<PmsCategory>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                  @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<PmsCategory> categoryList = categoryService.list(keyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(categoryList));
    }

    @ApiOperation("修改分类状态")
    @PostMapping("/updateStatus/{id}")
    public CommonResult updateStatus(@PathVariable Long id, @RequestParam(value = "status") Integer status) {
        PmsCategory umsRole = new PmsCategory();
        umsRole.setStatus(status);
        int count = categoryService.update(id, umsRole);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("删除分类")
    @PostMapping("/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        int count = categoryService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}
