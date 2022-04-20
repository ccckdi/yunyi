package com.cy.yunyi.portal.service.impl;

import com.cy.yunyi.portal.service.OrderGoodsService;
import com.cy.yunyi.mapper.OmsOrderGoodsMapper;
import com.cy.yunyi.model.OmsOrderGoods;
import com.cy.yunyi.model.OmsOrderGoodsExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author caihx
 * @Description:
 * @Date 2022/1/27
 */
@Service
public class OrderGoodsServiceImpl implements OrderGoodsService {

    @Autowired
    private OmsOrderGoodsMapper orderGoodsMapper;

    @Override
    public void create(OmsOrderGoods orderGoods) {
        orderGoodsMapper.insert(orderGoods);
    }

    @Override
    public List<OmsOrderGoods> queryByOrderId(Long id) {
        OmsOrderGoodsExample example = new OmsOrderGoodsExample();
        example.or().andOrderIdEqualTo(id).andStatusEqualTo(1);
        return orderGoodsMapper.selectByExample(example);
    }
}
