package com.cy.yunyi.admin.controller;

import com.cy.yunyi.admin.service.PmsGoodsAttributeService;
import com.cy.yunyi.common.api.CommonPage;
import com.cy.yunyi.common.api.CommonResult;
import com.cy.yunyi.model.PmsGoodsAttribute;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* @Author: chx
* @Description: 商品参数管理Controller
* @DateTime: 2021/11/30 10:29
**/
@RestController
@Api(tags = "PmsGoodsAttributeController", description = "商品参数管理")
@RequestMapping("/attribute")
public class PmsGoodsAttributeController {

    @Autowired
    private PmsGoodsAttributeService attributeService;

    @ApiOperation("添加参数")
    @PostMapping("/create")
    public CommonResult create(@RequestBody PmsGoodsAttribute attribute) {
        int count = attributeService.create(attribute);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("修改参数")
    @PostMapping("/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody PmsGoodsAttribute attribute) {
        int count = attributeService.update(id, attribute);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("根据参数名称分页获取参数列表")
    @GetMapping("list")
    public CommonResult<CommonPage<PmsGoodsAttribute>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                  @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<PmsGoodsAttribute> attributeList = attributeService.list(keyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(attributeList));
    }

    @ApiOperation("根据商品id获取参数列表")
    @GetMapping("listByGoodId")
    public CommonResult<CommonPage<PmsGoodsAttribute>> listByGoodId(@RequestParam(value = "goodId") Long goodId) {
        List<PmsGoodsAttribute> attributeList = attributeService.listByGoodId(goodId);
        return CommonResult.success(CommonPage.restPage(attributeList));
    }

    @ApiOperation("修改参数状态")
    @PostMapping("/updateStatus/{id}")
    public CommonResult updateStatus(@PathVariable Long id, @RequestParam(value = "status") Integer status) {
        PmsGoodsAttribute umsRole = new PmsGoodsAttribute();
        umsRole.setStatus(status);
        int count = attributeService.update(id, umsRole);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("删除参数")
    @PostMapping("/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        int count = attributeService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}
