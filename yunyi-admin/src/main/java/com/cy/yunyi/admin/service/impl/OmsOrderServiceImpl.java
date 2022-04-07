package com.cy.yunyi.admin.service.impl;

import com.cy.yunyi.admin.service.OmsOrderService;
import com.cy.yunyi.mapper.OmsOrderMapper;
import com.cy.yunyi.model.OmsOrder;
import com.cy.yunyi.model.OmsOrderExample;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author: chx
 * @Description: 订单管理Service实现类
 * @DateTime: 2021/12/2 20:31
 **/
@Service
public class OmsOrderServiceImpl implements OmsOrderService {

    @Autowired
    private OmsOrderMapper orderMapper;

    @Override
    public List<OmsOrder> list(String orderSn, String receiverKeyword, Integer status, Date createTime, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        OmsOrderExample example = new OmsOrderExample();
        OmsOrderExample.Criteria criteria1 = example.or();
        OmsOrderExample.Criteria criteria2 = example.or();
        if (!StringUtils.isEmpty(orderSn)){
            criteria1.andOrderSnEqualTo(orderSn);
            criteria2.andOrderSnEqualTo(orderSn);
        }
        if (!StringUtils.isEmpty(receiverKeyword)){
            criteria1.andConsigneeEqualTo(receiverKeyword);
            criteria2.andMobileEqualTo(receiverKeyword);
        }
        if (status != null){
            criteria1.andOrderStatusEqualTo(status);
            criteria2.andOrderStatusEqualTo(status);
        }
        if (createTime != null){
            Calendar cal = Calendar.getInstance();
            cal.setTime(createTime);//设置起时间
            cal.add(Calendar.DATE, 1);//增加一天
            Date endTime = cal.getTime();
            criteria1.andCreateTimeBetween(createTime,endTime);
            criteria2.andCreateTimeBetween(createTime,endTime);
        }

        criteria1.andStatusEqualTo(1);
        criteria2.andStatusEqualTo(1);

        List<OmsOrder> orderList = orderMapper.selectByExample(example);
        return orderList;
    }
}
