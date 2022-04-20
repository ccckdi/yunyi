package com.cy.yunyi.portal.vo;

import lombok.Data;

/**
 * @Author: chx
 * @Description: 个人中心返回封装
 * @DateTime: 2021/1/26 11:36
 **/
@Data
public class UserContentVo {
    //待付款
    private Integer unpaid = 0;
    //待发货
    private Integer unship = 0;
    //待收货
    private Integer unrecv = 0;
    //待评价
    private Integer uncomment = 0;
}
