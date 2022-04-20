package com.cy.yunyi.portal.dto;

import lombok.Data;

/**
 * @Author: chx
 * @Description: 提交订单Dto
 * @DateTime: 2021/12/30 20:49
 **/
@Data
public class SubmitOrderDto {
    private Long cartId;
    private Long addressId;
//    private Long couponId;
//    private Long userCouponId;
    private String message;
//    private Long grouponRulesId;
//    private Long grouponLinkId;
}
