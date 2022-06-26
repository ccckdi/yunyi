package com.cy.yunyi.portal.component;

import com.cy.yunyi.mapper.OmsOrderMapper;
import com.cy.yunyi.model.OmsOrder;
import com.cy.yunyi.portal.service.OrderService;
import com.cy.yunyi.portal.util.OrderHandleOption;
import com.cy.yunyi.portal.util.OrderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 取消订单消息的消费者
 * Created by macro on 2018/9/14.
 */
@Component
@RabbitListener(queues = "yunyi.order.cancel")
public class CancelOrderReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(com.cy.yunyi.portal.component.CancelOrderReceiver.class);
    @Autowired
    private OrderService orderService;

    @Autowired
    private OmsOrderMapper orderMapper;
    @RabbitHandler
    public void handle(Long orderId){
        OmsOrder order = orderMapper.selectByPrimaryKey(orderId);
        if (order.getOrderStatus() != null &&
                OrderUtil.STATUS_CREATE == order.getOrderStatus()){
            orderService.cancel(orderId);
        }
        LOGGER.info("process orderId:{}",orderId);
    }
}
