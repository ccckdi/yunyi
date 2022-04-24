package com.cy.yunyi.admin.service;

import com.cy.yunyi.admin.vo.IndexVo;
import com.cy.yunyi.admin.vo.LineChartDataVo;

/**
 * @Author caihx
 * @Description: 首页service
 * @Date 2022/4/22
 */
public interface IndexService {

    /**
     * 获取首页数据
     * @return
     */
    IndexVo index();

    /**
     * 获取折线图数据
     * @return
     */
    LineChartDataVo getLineChartData();
}
