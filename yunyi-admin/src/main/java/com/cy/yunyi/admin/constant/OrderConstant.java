package com.cy.yunyi.admin.constant;

import com.cy.yunyi.model.OmsOrder;

import java.util.ArrayList;
import java.util.List;

/*
 * 订单流程：下单成功－》支付订单－》发货－》收货
 * 订单状态：
 * 101 订单生成，未支付；102，下单未支付用户取消；103，下单未支付超期系统自动取消
 * 201 支付完成，商家未发货；202，订单生产，已付款未发货，用户申请退款；203，管理员执行退款操作，确认退款成功；
 * 301 商家发货，用户未确认；
 * 401 用户确认收货，订单结束； 402 用户没有确认收货，但是快递反馈已收货后，超过一定时间，系统自动确认收货，订单结束。
 *
 * 当101用户未付款时，此时用户可以进行的操作是取消或者付款
 * 当201支付完成而商家未发货时，此时用户可以退款
 * 当301商家已发货时，此时用户可以有确认收货
 * 当401用户确认收货以后，此时用户可以进行的操作是退货、删除、去评价或者再次购买
 * 当402系统自动确认收货以后，此时用户可以删除、去评价、或者再次购买
 */
public class OrderConstant {
    public static final int STATUS_CREATE = 101;
    public static final int STATUS_PAY = 201;
    public static final int STATUS_SHIP = 301;
    public static final int STATUS_CONFIRM = 401;
    public static final int STATUS_CANCEL = 102;
    public static final int STATUS_AUTO_CANCEL = 103;
    public static final int STATUS_ADMIN_CANCEL = 104;
    public static final int STATUS_REFUND = 202;
    public static final int STATUS_REFUND_CONFIRM = 203;
    public static final int STATUS_REFUND_REFUSED = 204;
    public static final int STATUS_AUTO_CONFIRM = 402;
}
