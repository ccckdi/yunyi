package com.cy.yunyi.admin.service;

import com.alipay.api.AlipayApiException;
import com.cy.yunyi.admin.vo.OmsOrderDetailsVo;
import com.cy.yunyi.common.vo.PayAsyncVo;
import com.cy.yunyi.model.OmsOrder;

import java.util.Date;
import java.util.List;

/**
 * @Author: chx
 * @Description: 订单管理Service
 * @DateTime: 2021/12/2 20:30
 **/
public interface OmsOrderService {
    List<OmsOrder> list(String orderSn, String receiverKeyword, Integer orderStatus, Date createTime, Integer pageSize, Integer pageNum);

    OmsOrderDetailsVo detail(Long id);

    /**
     * 退款列表
     * @param orderSn
     * @param receiverKeyword
     * @param orderStatus
     * @param updateTime
     * @param pageSize
     * @param pageNum
     * @return
     */
    List<OmsOrder> refundList(String orderSn, String receiverKeyword, Integer orderStatus, Date updateTime, Integer pageSize, Integer pageNum);

    int update(Long id, OmsOrder order);

    int delete(Long id);

    /**
     * 确认退款
     * @return
     * @param id
     */
    boolean agreeRefund(Long id) throws AlipayApiException;

    /**
     * 拒绝退款
     * @return
     * @param id
     */
    boolean refusedRefund(Long id) throws AlipayApiException;
}
