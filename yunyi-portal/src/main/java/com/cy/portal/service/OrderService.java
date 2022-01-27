package com.cy.portal.service;

import com.cy.portal.dto.SubmitOrderDto;
import com.cy.portal.vo.OrderVo;
import com.cy.yunyi.model.OmsOrder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: chx
 * @Description: 订单Service
 * @DateTime: 2021/12/30 20:07
 **/
public interface OrderService {

    @Transactional
    void submit(Long userId, SubmitOrderDto submitOrderDto);

    /**
     * 订单列表
     *
     * @param userId   用户ID
     * @param showType 订单信息：
     *                 0，全部订单；
     *                 1，待付款；
     *                 2，待发货；
     *                 3，待收货；
     *                 4，待评价。
     * @param pageSize     分页页数
     * @param pageNum     分页大小
     * @return 订单列表
     */
    List<OrderVo> list(Long userId, Integer showType, Integer pageSize, Integer pageNum);
}
