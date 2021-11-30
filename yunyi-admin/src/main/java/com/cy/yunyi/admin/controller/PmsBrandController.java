package com.cy.yunyi.admin.controller;

import com.cy.yunyi.admin.service.PmsBrandService;
import com.cy.yunyi.common.api.CommonPage;
import com.cy.yunyi.common.api.CommonResult;
import com.cy.yunyi.model.PmsBrand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* @Author: chx
* @Description: 品牌管理Controller
* @DateTime: 2021/11/30 10:29
**/
@RestController
@Api(tags = "PmsBrandController", description = "品牌管理")
@RequestMapping("/brand")
public class PmsBrandController {

    @Autowired
    private PmsBrandService brandService;

    @ApiOperation("添加品牌")
    @PostMapping("/create")
    public CommonResult create(@RequestBody PmsBrand brand) {
        int count = brandService.create(brand);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("修改品牌")
    @PostMapping("/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody PmsBrand brand) {
        int count = brandService.update(id, brand);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("根据品牌名称分页获取品牌列表")
    @GetMapping("list")
    public CommonResult<CommonPage<PmsBrand>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                  @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<PmsBrand> brandList = brandService.list(keyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(brandList));
    }

    @ApiOperation("修改品牌状态")
    @PostMapping("/updateStatus/{id}")
    public CommonResult updateStatus(@PathVariable Long id, @RequestParam(value = "status") Integer status) {
        PmsBrand umsRole = new PmsBrand();
        umsRole.setStatus(status);
        int count = brandService.update(id, umsRole);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("删除品牌")
    @PostMapping("/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        int count = brandService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}
