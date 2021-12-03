package com.cy.yunyi.admin.service.impl;

import com.cy.yunyi.admin.service.OmsOrderService;
import com.cy.yunyi.mapper.OmsOrderMapper;
import com.cy.yunyi.model.OmsOrder;
import com.cy.yunyi.model.OmsOrderExample;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    public List<OmsOrder> list(String keyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        OmsOrderExample example = new OmsOrderExample();
        if (!StringUtils.isEmpty(keyword)){
            example.createCriteria().andOrderSnEqualTo(keyword);
        }
        List<OmsOrder> orderList = orderMapper.selectByExample(example);
        return orderList;
    }
}
