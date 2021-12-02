package com.cy.yunyi.admin.controller;

import com.cy.yunyi.admin.service.PmsGoodsSpecificationService;
import com.cy.yunyi.common.api.CommonPage;
import com.cy.yunyi.common.api.CommonResult;
import com.cy.yunyi.model.PmsGoodsSpecification;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* @Author: chx
* @Description: 商品规格管理Controller
 * TODO 参数校验
* @DateTime: 2021/12/1 10:29
**/
@RestController
@Api(tags = "PmsGoodsSpecificationController", description = "商品规格管理")
@RequestMapping("/specification")
public class PmsGoodsSpecificationController {

    @Autowired
    private PmsGoodsSpecificationService specificationService;

    @ApiOperation("添加规格")
    @PostMapping("/create")
    public CommonResult create(@RequestBody PmsGoodsSpecification specification) {
        int count = specificationService.create(specification);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("修改规格")
    @PostMapping("/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody PmsGoodsSpecification specification) {
        int count = specificationService.update(id, specification);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("根据规格名称分页获取规格列表")
    @GetMapping("list")
    public CommonResult<CommonPage<PmsGoodsSpecification>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                  @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<PmsGoodsSpecification> specificationList = specificationService.list(keyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(specificationList));
    }

    @ApiOperation("根据商品id获取规格列表")
    @GetMapping("listByGoodId")
    public CommonResult<CommonPage<PmsGoodsSpecification>> listByGoodId(@RequestParam(value = "goodId") Long goodId) {
        List<PmsGoodsSpecification> specificationList = specificationService.listByGoodId(goodId);
        return CommonResult.success(CommonPage.restPage(specificationList));
    }

    @ApiOperation("修改规格状态")
    @PostMapping("/updateStatus/{id}")
    public CommonResult updateStatus(@PathVariable Long id, @RequestParam(value = "status") Integer status) {
        PmsGoodsSpecification umsRole = new PmsGoodsSpecification();
        umsRole.setStatus(status);
        int count = specificationService.update(id, umsRole);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("删除规格")
    @PostMapping("/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        int count = specificationService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}
