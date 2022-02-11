package com.cy.portal.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author caihx
 * @Description: 浏览记录Vo
 * @Date 2022/2/11
 */
@Data
public class FootprintVo {
    private Long id;
    private Long goodsId;
    private Date createTime;
    private String name;
    private String brief;
    private String icon;
    private BigDecimal retailPrice;
}
