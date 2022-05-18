package com.cy.yunyi.portal.service;

import com.alipay.api.AlipayApiException;
import com.cy.yunyi.common.vo.PayAsyncVo;
import com.cy.yunyi.portal.dto.SubmitOrderDto;
import com.cy.yunyi.model.OmsOrder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Author: chx
 * @Description: 订单Service
 * @DateTime: 2021/12/30 20:07
 **/
public interface OrderService {

    /**
     * 提交订单
     * @param userId
     * @param submitOrderDto
     * @return
     */
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

    Map<String,Object> detail(Long userId, Long orderId);

    Integer cancel(Long userId, Long orderId);

    /**
     * 系统超时取消
     * @param orderId
     * @return
     */
    Integer cancel(Long orderId);

    /**
     * 支付宝支付
     * @param orderSn
     * @return
     * @throws AlipayApiException
     */
    String aliPay(String orderSn) throws AlipayApiException;

    /**
     * 支付回调
     * @param vo
     * @return
     */
    @Transactional
    Integer payNotify(PayAsyncVo vo);

    /**
     * 确认收货
     * @param userId
     * @param orderId
     * @return
     */
    Integer confirm(Long userId, Long orderId);

    Integer refund(Long userId, Long orderId);

    List<OmsOrder> queryByUserId(Long userId);

    /**
     * 去付款
     * @param userId
     * @param orderId
     * @return orderSn
     */
    String prepay(Long userId, Long orderId);

    /**
     * 删除订单
     * @param userId
     * @param orderId
     * @return
     */
    Integer delete(Long userId, Long orderId);
}
