package com.cy.yunyi.admin.controller;

import com.cy.yunyi.admin.service.IndexService;
import com.cy.yunyi.admin.vo.IndexVo;
import com.cy.yunyi.admin.vo.LineChartDataVo;
import com.cy.yunyi.admin.vo.OmsOrderDetailsVo;
import com.cy.yunyi.common.api.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author caihx
 * @Description: 首页管理
 * @Date 2022/4/22
 */
@RestController
@Api(tags = "IndexController", description = "首页管理")
@RequestMapping("/order")
@Slf4j
public class IndexController {

    @Autowired
    private IndexService indexService;

    // TODO 数据存储到redis
    @ApiOperation("获取首页数据")
    @GetMapping(value = "/index")
    public CommonResult<IndexVo> index() {
        IndexVo indexVo = indexService.index();
        return CommonResult.success(indexVo);
    }

    // 折线图数据
    @ApiOperation("获取首页折线图数据")
    @GetMapping(value = "/getLineChartData")
    public CommonResult<LineChartDataVo> getLineChartData() {
        LineChartDataVo lineChartDataVo = indexService.getLineChartData();
        return CommonResult.success(lineChartDataVo);
    }
}
