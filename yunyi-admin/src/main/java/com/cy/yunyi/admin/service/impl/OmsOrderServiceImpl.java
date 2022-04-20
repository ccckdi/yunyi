package com.cy.yunyi.admin.service.impl;

import com.alipay.api.AlipayApiException;
import com.cy.yunyi.admin.constant.OrderConstant;
import com.cy.yunyi.admin.service.OmsOrderService;
import com.cy.yunyi.admin.vo.OmsOrderDetailsVo;
import com.cy.yunyi.common.notify.NotifyType;
import com.cy.yunyi.common.util.AlipayUtil;
import com.cy.yunyi.common.vo.PayAsyncVo;
import com.cy.yunyi.common.vo.RefundVo;
import com.cy.yunyi.mapper.OmsOrderGoodsMapper;
import com.cy.yunyi.mapper.OmsOrderMapper;
import com.cy.yunyi.mapper.UmsMemberMapper;
import com.cy.yunyi.model.*;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
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

    @Autowired
    private UmsMemberMapper memberMapper;

    @Autowired
    private OmsOrderGoodsMapper orderGoodsMapper;

    @Autowired
    private AlipayUtil alipayUtil;

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

    @Override
    public OmsOrderDetailsVo detail(Long id) {
        OmsOrderDetailsVo omsOrderDetailsVo = new OmsOrderDetailsVo();

        //订单信息
        OmsOrderExample orderExample = new OmsOrderExample();
        orderExample.createCriteria().andIdEqualTo(id).andStatusEqualTo(1);
        List<OmsOrder> orderList = orderMapper.selectByExample(orderExample);
        if (orderList != null && orderList.size() > 0){
            BeanUtils.copyProperties(orderList.get(0), omsOrderDetailsVo);
        }

        //商品列表
        OmsOrderGoodsExample orderGoodsExample = new OmsOrderGoodsExample();
        OmsOrderGoodsExample.Criteria criteria = orderGoodsExample.createCriteria();
        criteria.andOrderIdEqualTo(id);
        criteria.andStatusEqualTo(1);
        List<OmsOrderGoods> omsOrderGoodsList = orderGoodsMapper.selectByExample(orderGoodsExample);
        omsOrderDetailsVo.setOmsOrderGoodsList(omsOrderGoodsList);
        return omsOrderDetailsVo;
    }

    /**
     * 确认退款
     * @return
     * @param id
     */
    @Override
    public boolean refund(Long id) throws AlipayApiException {
        //订单信息
        OmsOrderExample orderExample = new OmsOrderExample();
        orderExample.createCriteria().andIdEqualTo(id).andStatusEqualTo(1);
        List<OmsOrder> orderList = orderMapper.selectByExample(orderExample);
        RefundVo refundVo = new RefundVo();
        if (orderList != null && orderList.size() > 0){
            OmsOrder omsOrder = orderList.get(0);
            refundVo.setTradeNo(omsOrder.getPayId());
            refundVo.setRefundAmount(String.valueOf(omsOrder.getActualPrice()));
            refundVo.setOutRequestNo(String.valueOf(omsOrder.getUserId()));
        }
        boolean result = alipayUtil.Refund(refundVo);
        return result;
    }

    /**
     * 支付回调
     * @param vo
     * @return
     */
    @Override
    public Integer payNotify(PayAsyncVo vo) {
        //查询订单
        OmsOrderExample example = new OmsOrderExample();
        OmsOrderExample.Criteria criteria = example.createCriteria();
        criteria.andOrderSnEqualTo(vo.getOut_trade_no());
        List<OmsOrder> orderList = orderMapper.selectByExample(example);
        if (orderList.size() > 0){
            //修改订单状态
            OmsOrder order = orderList.get(0);
            order.setOrderStatus(OrderConstant.STATUS_REFUND_CONFIRM);
//            order.setRefundTime();
            order.setUpdateTime(new Date());
            int count = orderMapper.updateByPrimaryKey(order);

//            //异步短信通知用户
//            //订单编号由于受腾讯云参数长度现在只保留后6位
//            notifyService.notifySmsTemplate(order.getMobile(), NotifyType.PAY_SUCCEED,
//                    new String[]{order.getOrderSn().substring(order.getOrderSn().length() - 6,order.getOrderSn().length())});
//
//            //异步邮件通知管理员
//            notifyService.notifyMail("新订单通知", order.toString());

            return count;
        }
        return 0;
    }
}
