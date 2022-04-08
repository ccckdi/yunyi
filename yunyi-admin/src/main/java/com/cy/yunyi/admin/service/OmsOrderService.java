package com.cy.yunyi.admin.service;

import com.cy.yunyi.admin.vo.OmsOrderDetailsVo;
import com.cy.yunyi.model.OmsOrder;

import java.util.Date;
import java.util.List;

/**
 * @Author: chx
 * @Description: 订单管理Service
 * @DateTime: 2021/12/2 20:30
 **/
public interface OmsOrderService {
    List<OmsOrder> list(String orderSn,String receiverKeyword,Integer status, Date createTime, Integer pageSize, Integer pageNum);

    OmsOrderDetailsVo detail(Long id);
}
