package com.cy.portal.controller;

import com.cy.portal.annotation.LoginUser;
import com.cy.portal.controller.validator.Order;
import com.cy.portal.controller.validator.Sort;
import com.cy.portal.service.FootprintService;
import com.cy.portal.service.GoodsService;
import com.cy.portal.vo.FootprintVo;
import com.cy.yunyi.common.api.CommonPage;
import com.cy.yunyi.common.api.CommonResult;
import com.cy.yunyi.model.PmsGoods;
import com.cy.yunyi.model.RmsFootprint;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author caihx
 * @Description:
 * @Date 2022/2/11
 */
@RestController
@Api(tags = "FootprintController", description = "浏览足迹管理")
@RequestMapping("/footprint")
public class FootprintController {

    @Autowired
    private FootprintService footprintService;

    @Autowired
    private GoodsService goodsService;

    /**
     * 分页获取浏览历史
     */
    @ApiOperation("分页获取浏览历史")
    @GetMapping("/list")
    public CommonResult<CommonPage<FootprintVo>> list(@LoginUser Long userId,
                                                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                       @Sort @RequestParam(defaultValue = "create_time") String sort,
                                                       @Order @RequestParam(defaultValue = "desc") String order){
        if (userId == null) {
            return CommonResult.validateFailed();
        }

        List<RmsFootprint> footprintList = footprintService.list(userId,pageNum,pageSize,sort,order);
        List<FootprintVo> footprintVoList = new ArrayList<>(footprintList.size());
        for (RmsFootprint footprint : footprintList) {
            FootprintVo footprintVo = new FootprintVo();
            BeanUtils.copyProperties(footprint,footprintVo);
            PmsGoods goods = goodsService.getById(footprint.getGoodsId());
            footprintVo.setName(goods.getName());
            footprintVo.setBrief(goods.getBrief());
            footprintVo.setIcon(goods.getIcon());
            footprintVo.setRetailPrice(goods.getRetailPrice());
            footprintVoList.add(footprintVo);
        }
        return CommonResult.success(CommonPage.restPageByList(footprintVoList,footprintList));
    }
}
