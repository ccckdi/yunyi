package com.cy.yunyi.admin.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author caihx
 * @Description: 折线图数据
 * @Date 2022/4/22
 */
@Data
public class LineChartDataVo {
    private Date date;
    private Integer orderCount;
    private BigDecimal orderAmount;
}
