package com.cy.yunyi.admin.service;

import com.cy.yunyi.model.OmsOrder;

import java.util.List;

/**
 * @Author: chx
 * @Description: 订单管理Service
 * @DateTime: 2021/12/2 20:30
 **/
public interface OmsOrderService {
    List<OmsOrder> list(String keyword, Integer pageSize, Integer pageNum);
}
