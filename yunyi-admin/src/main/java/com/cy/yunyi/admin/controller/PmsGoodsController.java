package com.cy.yunyi.admin.controller;

import com.cy.yunyi.admin.dto.PmsGoodsAllParam;
import com.cy.yunyi.admin.service.PmsGoodsService;
import com.cy.yunyi.common.api.CommonPage;
import com.cy.yunyi.common.api.CommonResult;
import com.cy.yunyi.model.PmsGoods;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* @Author: chx
* @Description: 商品管理Controller
* @DateTime: 2021/12/2 16:35
**/
@RestController
@Api(tags = "PmsGoodsController", description = "商品管理")
@RequestMapping("/goods")
public class PmsGoodsController {

    @Autowired
    private PmsGoodsService goodsService;

    @ApiOperation("添加商品")
    @PostMapping("/create")
    public CommonResult create(@RequestBody PmsGoodsAllParam goodsAllParam) {
        CommonResult commonResult = goodsService.create(goodsAllParam);
        return commonResult;
    }

    @ApiOperation("修改商品")
    @PostMapping("/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody PmsGoodsAllParam goodsAllParam) {
        CommonResult commonResult = goodsService.update(id, goodsAllParam);
        return commonResult;
    }

    @ApiOperation("修改商品详情")
    @GetMapping("/updateInfo/{id}")
    public CommonResult updateInfo(@PathVariable Long id) {
        PmsGoodsAllParam goodsAllParam = goodsService.updateInfo(id);
        return CommonResult.success(goodsAllParam);
    }

    @ApiOperation("根据商品名称分页获取商品列表")
    @GetMapping("list")
    public CommonResult<CommonPage<PmsGoods>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                  @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<PmsGoods> goodsList = goodsService.list(keyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(goodsList));
    }

    @ApiOperation("修改商品状态")
    @PostMapping("/updateStatus/{id}")
    public CommonResult updateStatus(@PathVariable Long id, @RequestParam(value = "status") Integer status) {
        PmsGoods pmsGoods = new PmsGoods();
        pmsGoods.setStatus(status);
        int count = goodsService.update(id, pmsGoods);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("删除商品")
    @PostMapping("/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        int count = goodsService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}
