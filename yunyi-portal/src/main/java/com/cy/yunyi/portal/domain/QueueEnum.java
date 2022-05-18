package com.cy.yunyi.portal.domain;

import lombok.Getter;

/**
 * @Author caihx
 * @Description: 消息队列枚举配置
 * @Date 2022/5/18
 */
@Getter
public enum QueueEnum {
    /**
     * 消息通知队列
     */
    QUEUE_ORDER_CANCEL("yunyi.order.direct", "yunyi.order.cancel", "yunyi.order.cancel"),
    /**
     * 消息通知ttl队列
     */
    QUEUE_TTL_ORDER_CANCEL("yunyi.order.direct.ttl", "yunyi.order.cancel.ttl", "yunyi.order.cancel.ttl");

    /**
     * 交换名称
     */
    private String exchange;
    /**
     * 队列名称
     */
    private String name;
    /**
     * 路由键
     */
    private String routeKey;

    QueueEnum(String exchange, String name, String routeKey) {
        this.exchange = exchange;
        this.name = name;
        this.routeKey = routeKey;
    }
}