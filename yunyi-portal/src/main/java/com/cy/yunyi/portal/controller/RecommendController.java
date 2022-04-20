package com.cy.yunyi.portal.controller;

import com.cy.yunyi.portal.annotation.LoginUser;
import com.cy.yunyi.portal.recommend.CalculateItemScore;
import com.cy.yunyi.common.api.CommonResult;
import com.cy.yunyi.model.PmsGoods;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/doRecommend")
    public CommonResult doRecommend(@LoginUser Long userId) {
        if (userId == null) {
            return CommonResult.validateFailed();
        }
        List<PmsGoods> goodsList = calculateItemScore.recommendByUserId(userId);
        return CommonResult.success(goodsList);
    }

    @ApiOperation("计算用户喜好物品得分")
    @GetMapping("/compute")
    public CommonResult compute(@LoginUser Long userId) {
        if (userId == null) {
            return CommonResult.validateFailed();
        }

        calculateItemScore.computeResultByUserId(userId);
        return CommonResult.success();
    }
}
