package com.cy.portal.dto;

import lombok.Data;

/**
 * @Author: chx
 * @Description: 确认订单Dto
 * @DateTime: 2021/12/30 20:19
 **/
@Data
public class CheckoutOrderDto {
    private Long cartId;
    private Long addressId;
    private Long couponId;
    private Long userCouponId;
    private Long grouponRulesId;
}
