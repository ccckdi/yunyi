package com.cy.portal.service;

import com.cy.portal.dto.SubmitOrderDto;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: chx
 * @Description: 订单Service
 * @DateTime: 2021/12/30 20:07
 **/
public interface OrderService {

    @Transactional
    void submit(Long userId, SubmitOrderDto submitOrderDto);
}
