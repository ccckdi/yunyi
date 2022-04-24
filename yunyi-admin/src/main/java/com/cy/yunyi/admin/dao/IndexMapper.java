package com.cy.yunyi.admin.dao;

import com.cy.yunyi.admin.vo.IndexVo;
import com.cy.yunyi.admin.vo.LineChartDataVo;

/**
 * @Author: chx
 * @Description: TODO
 * @DateTime: 2022/4/24 22:11
 **/
public interface IndexMapper {

    IndexVo index();
    LineChartDataVo getLineChartData();
}
