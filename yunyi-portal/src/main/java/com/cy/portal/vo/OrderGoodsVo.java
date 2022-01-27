package com.cy.portal.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author caihx
 * @Description:
 * @Date 2022/1/27
 */
@Data
public class OrderGoodsVo {
    private Long id;
    private String goodsName;
    private Integer number;
    private String picUrl;
    private String[] specifications;
    private BigDecimal price;
}
