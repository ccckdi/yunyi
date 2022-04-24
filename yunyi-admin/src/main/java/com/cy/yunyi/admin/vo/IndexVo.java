package com.cy.yunyi.admin.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author caihx
 * @Description: 首页数据Vo
 * @Date 2022/4/22
 */
@Data
public class IndexVo {
    //商城总览
    private Integer totalOrdersToday;
    private BigDecimal totalSalesToday;
    private BigDecimal totalSalesYesterday;

    //待处理事务
    private Integer pendingPayment;
    private Integer pendingOrder;
    private Integer pendingRefund;

    //商品总览
    private Integer upGoods;
    private Integer downGoods;
    private Integer smallGoods;
    private Integer totalGoods;

    //用户总览
    private Integer addedToday;
    private Integer addedYesterday;
    private Integer addedThisMonth;
    private Integer totalMembers;

    //订单统计
    private LineChartDataVo lineChartDataVo;
}
