package com.cy.portal.service.impl;

import com.cy.portal.service.UserService;
import com.cy.portal.util.OrderUtil;
import com.cy.portal.vo.UserContentVo;
import com.cy.yunyi.mapper.OmsOrderMapper;
import com.cy.yunyi.model.OmsOrder;
import com.cy.yunyi.model.OmsOrderExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: chx
 * @Description: 个人中心Service实现类
 * @DateTime: 2022/1/26 11:46
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private OmsOrderMapper orderMapper;

    @Override
    public UserContentVo content(Long userId) {

        //查询该用户的订单
        OmsOrderExample example = new OmsOrderExample();
        example.or().andUserIdEqualTo(userId).andStatusEqualTo(1);
        List<OmsOrder> orders = orderMapper.selectByExample(example);

        int unpaid = 0;
        int unship = 0;
        int unrecv = 0;
        int uncomment = 0;
        for (OmsOrder order : orders) {
            if (OrderUtil.isCreateStatus(order)) {
                unpaid++;
            } else if (OrderUtil.isPayStatus(order)) {
                unship++;
            } else if (OrderUtil.isShipStatus(order)) {
                unrecv++;
            } else if (OrderUtil.isConfirmStatus(order) || OrderUtil.isAutoConfirmStatus(order)) {
                uncomment ++;
            } else {
                // do nothing
            }
        }

        UserContentVo userContentVo = new UserContentVo();
        userContentVo.setUnpaid(unpaid);
        userContentVo.setUnship(unship);
        userContentVo.setUnrecv(unrecv);
        userContentVo.setUncomment(uncomment);
        return userContentVo;
    }
}
