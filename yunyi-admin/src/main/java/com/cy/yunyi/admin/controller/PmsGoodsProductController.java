package com.cy.yunyi.admin.controller;

import com.cy.yunyi.admin.service.PmsGoodsProductService;
import com.cy.yunyi.common.api.CommonPage;
import com.cy.yunyi.common.api.CommonResult;
import com.cy.yunyi.model.PmsGoodsProduct;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* @Author: chx
* @Description: 商品库存管理Controller
 * TODO 参数校验
* @DateTime: 2021/12/1 10:29
**/
@RestController
@Api(tags = "PmsGoodsProductController", description = "商品库存管理")
@RequestMapping("/product")
public class PmsGoodsProductController {

    @Autowired
    private PmsGoodsProductService productService;

    @ApiOperation("添加库存")
    @PostMapping("/create")
    public CommonResult create(@RequestBody PmsGoodsProduct product) {
        int count = productService.create(product);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("修改库存")
    @PostMapping("/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody PmsGoodsProduct product) {
        int count = productService.update(id, product);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("根据库存名称分页获取库存列表")
    @GetMapping("list")
    public CommonResult<CommonPage<PmsGoodsProduct>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                          @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                          @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<PmsGoodsProduct> productList = productService.list(keyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(productList));
    }

    @ApiOperation("根据商品id获取库存列表")
    @GetMapping("listByGoodId")
    public CommonResult<CommonPage<PmsGoodsProduct>> listByGoodId(@RequestParam(value = "goodId") Long goodId) {
        List<PmsGoodsProduct> productList = productService.listByGoodId(goodId);
        return CommonResult.success(CommonPage.restPage(productList));
    }

    @ApiOperation("修改库存状态")
    @PostMapping("/updateStatus/{id}")
    public CommonResult updateStatus(@PathVariable Long id, @RequestParam(value = "status") Integer status) {
        PmsGoodsProduct umsRole = new PmsGoodsProduct();
        umsRole.setStatus(status);
        int count = productService.update(id, umsRole);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("删除库存")
    @PostMapping("/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        int count = productService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}
