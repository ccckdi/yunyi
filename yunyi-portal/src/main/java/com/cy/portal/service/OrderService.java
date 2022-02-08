package com.cy.portal.service;

import com.alipay.api.AlipayApiException;
import com.cy.portal.dto.PayAsyncVo;
import com.cy.portal.dto.SubmitOrderDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @Author: chx
 * @Description: 订单Service
 * @DateTime: 2021/12/30 20:07
 **/
public interface OrderService {

    @Transactional
    String submit(Long userId, SubmitOrderDto submitOrderDto);

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
    Map<String,Object> list(Long userId, Integer showType, Integer pageSize, Integer pageNum);

    String aliPay(String orderSn) throws AlipayApiException;

    @Transactional
    Integer payNotify(PayAsyncVo vo);
}
