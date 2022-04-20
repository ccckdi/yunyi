package com.cy.yunyi.portal.vo;

import com.cy.yunyi.model.OmsCart;
import com.cy.yunyi.model.UmsAddress;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: chx
 * @Description: 确认订单Vo
 * @DateTime: 2021/12/30 20:23
 **/
@Data
public class CheckoutOrderVo {
    private Long addressId;
    private Long couponId;
    private Long userCouponId;
    private Long cartId;
    private UmsAddress checkedAddress;
    private Integer availableCouponLength;
    private BigDecimal goodsTotalPrice;
    private BigDecimal freightPrice;
    private BigDecimal couponPrice;
    private BigDecimal orderTotalPrice;
    private BigDecimal actualPrice;
    private List<OmsCart> checkedGoodsList;
}
