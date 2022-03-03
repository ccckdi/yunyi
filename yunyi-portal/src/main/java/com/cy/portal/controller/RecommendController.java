package com.cy.portal.controller;

import com.cy.portal.annotation.LoginUser;
import com.cy.portal.recommend.CalculateItemScore;
import com.cy.yunyi.common.api.CommonResult;
import com.cy.yunyi.model.PmsGoods;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author caihx
 * @Description: 推荐服务Controller
 * @Date 2022/2/22
 */
@RestController
@Api(tags = "RecommendController", description = "推荐服务")
@RequestMapping("/recommend")
@Slf4j
public class RecommendController {

    @Autowired
    private CalculateItemScore calculateItemScore;

    @ApiOperation("进行推荐")
    @PostMapping("/doRecommend")
    public CommonResult doRecommend(@RequestParam Long userId) {
//        if (userId == null) {
//            return CommonResult.validateFailed();
//        }

        List<PmsGoods> goodsList = calculateItemScore.recommendByUserId(userId);
        return CommonResult.success(goodsList);
    }

    @ApiOperation("计算用户喜好物品得分")
    @PostMapping("/compute")
    public CommonResult compute(@RequestParam Long userId) {
//        if (userId == null) {
//            return CommonResult.validateFailed();
//        }

        calculateItemScore.computeResultByUserId(userId);
        return CommonResult.success();
    }
}
