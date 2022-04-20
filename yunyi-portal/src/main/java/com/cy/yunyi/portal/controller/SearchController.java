package com.cy.yunyi.portal.controller;

import com.cy.yunyi.portal.annotation.LoginUser;
import com.cy.yunyi.portal.service.SearchHistoryService;
import com.cy.yunyi.common.api.CommonResult;
import com.cy.yunyi.model.RmsSearchHistory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author caihx
 * @Description: 搜索模块Controller
 * @Date 2022/2/10
 */
@RestController
@Api(tags = "SearchController", description = "搜索模块")
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchHistoryService searchHistoryService;

    @ApiOperation("首页内容页信息展示")
    @GetMapping("/index")
    public CommonResult index(@LoginUser Long userId) {
        List<RmsSearchHistory> historyList = null;
        if (userId != null){
            historyList = searchHistoryService.getByUserId(userId);
        }
        Map<String, Object> data = new HashMap<String, Object>();
//        data.put("defaultKeyword", defaultKeyword);
//        data.put("hotKeywordList", hotKeywordList);
        data.put("historyKeywordList", historyList);
        return CommonResult.success(data);
    }

}
