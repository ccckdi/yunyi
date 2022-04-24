package com.cy.yunyi.admin.service.impl;

import cn.hutool.core.convert.Convert;
import com.alipay.api.AlipayApiException;
import com.alipay.api.response.AlipayTradeRefundResponse;
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

import java.math.BigDecimal;
import java.util.ArrayList;
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
    public List<OmsOrder> list(String orderSn, String receiverKeyword, Integer orderStatus, Date createTime, Integer pageSize, Integer pageNum) {
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
        if (orderStatus != null){
            criteria1.andOrderStatusEqualTo(orderStatus);
            criteria2.andOrderStatusEqualTo(orderStatus);
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

    @Override
    public List<OmsOrder> refundList(String orderSn, String receiverKeyword, Integer orderStatus, Date updateTime, Integer pageSize, Integer pageNum) {
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
        if (orderStatus != null){
            criteria1.andOrderStatusEqualTo(orderStatus);
            criteria2.andOrderStatusEqualTo(orderStatus);
        }else {
            ArrayList<Integer> statusList = new ArrayList<>();
            statusList.add(OrderConstant.STATUS_REFUND);
            statusList.add(OrderConstant.STATUS_REFUND_CONFIRM);
            statusList.add(OrderConstant.STATUS_REFUND_REFUSED);
            criteria1.andOrderStatusIn(statusList);
            criteria2.andOrderStatusIn(statusList);
        }
        if (updateTime != null){
            Calendar cal = Calendar.getInstance();
            cal.setTime(updateTime);//设置起时间
            cal.add(Calendar.DATE, 1);//增加一天
            Date endTime = cal.getTime();
            criteria1.andCreateTimeBetween(updateTime,endTime);
            criteria2.andCreateTimeBetween(updateTime,endTime);
        }

        criteria1.andStatusEqualTo(1);
        criteria2.andStatusEqualTo(1);

        List<OmsOrder> orderList = orderMapper.selectByExample(example);
        return orderList;
    }

    @Override
    public int update(Long id, OmsOrder order) {
        return 0;
    }

    @Override
    public int delete(Long id) {
        return 0;
    }

    /**
     * 确认退款
     * @return
     * @param id
     */
    @Override
    public boolean agreeRefund(Long id) throws AlipayApiException {
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
        AlipayTradeRefundResponse result = alipayUtil.Refund(refundVo);
        if (result.isSuccess()){
            //查询订单
            OmsOrderExample example = new OmsOrderExample();
            OmsOrderExample.Criteria criteria = example.createCriteria();
            criteria.andOrderSnEqualTo(result.getOutTradeNo());
            List<OmsOrder> refundOrderList = orderMapper.selectByExample(example);
            if (refundOrderList.size() > 0){
                //修改订单状态
                OmsOrder order = refundOrderList.get(0);
                order.setOrderStatus(OrderConstant.STATUS_REFUND_CONFIRM);
                order.setRefundAmount(new BigDecimal(result.getRefundFee()));
                order.setRefundTime(result.getGmtRefundPay());
                order.setUpdateTime(new Date());
                int count = orderMapper.updateByPrimaryKey(order);

//            //异步短信通知用户
//            //订单编号由于受腾讯云参数长度现在只保留后6位
//            notifyService.notifySmsTemplate(order.getMobile(), NotifyType.PAY_SUCCEED,
//                    new String[]{order.getOrderSn().substring(order.getOrderSn().length() - 6,order.getOrderSn().length())});
//
//            //异步邮件通知管理员
//            notifyService.notifyMail("新订单通知", order.toString());
            }
        }
        return result.isSuccess();
    }

    /**
     * 确认退款
     * @return
     * @param id
     */
    @Override
    public boolean refusedRefund(Long id) throws AlipayApiException {
        //订单信息
        OmsOrderExample orderExample = new OmsOrderExample();
        orderExample.createCriteria().andIdEqualTo(id).andStatusEqualTo(1);
        List<OmsOrder> orderList = orderMapper.selectByExample(orderExample);
        if (orderList != null && orderList.size() > 0){
            OmsOrder omsOrder = orderList.get(0);
            //修改订单状态
            omsOrder.setOrderStatus(OrderConstant.STATUS_REFUND_REFUSED);
            omsOrder.setUpdateTime(new Date());
            int count = orderMapper.updateByPrimaryKey(omsOrder);
            return count > 0;
        }


//        //异步短信通知用户
//        //订单编号由于受腾讯云参数长度现在只保留后6位
//        notifyService.notifySmsTemplate(order.getMobile(), NotifyType.PAY_SUCCEED,
//                new String[]{order.getOrderSn().substring(order.getOrderSn().length() - 6,order.getOrderSn().length())});
//
//        //异步邮件通知管理员
//        notifyService.notifyMail("新订单通知", order.toString());

        return false;
    }

//
//    /**
//     * 退款成功后修改订单状态
//     * @param outTradeNo
//     * @return
//     */
//    public Integer refundNotify(String outTradeNo) {
//        //查询订单
//        OmsOrderExample example = new OmsOrderExample();
//        OmsOrderExample.Criteria criteria = example.createCriteria();
//        criteria.andOrderSnEqualTo(outTradeNo);
//        List<OmsOrder> orderList = orderMapper.selectByExample(example);
//        if (orderList.size() > 0){
//            //修改订单状态
//            OmsOrder order = orderList.get(0);
//            order.setOrderStatus(OrderConstant.STATUS_REFUND_CONFIRM);
//            order.setRefundTime(Convert.toDate());
//            order.setUpdateTime(new Date());
//            int count = orderMapper.updateByPrimaryKey(order);
//
////            //异步短信通知用户
////            //订单编号由于受腾讯云参数长度现在只保留后6位
////            notifyService.notifySmsTemplate(order.getMobile(), NotifyType.PAY_SUCCEED,
////                    new String[]{order.getOrderSn().substring(order.getOrderSn().length() - 6,order.getOrderSn().length())});
////
////            //异步邮件通知管理员
////            notifyService.notifyMail("新订单通知", order.toString());
//
//            return count;
//        }
//        return 0;
//    }
}
