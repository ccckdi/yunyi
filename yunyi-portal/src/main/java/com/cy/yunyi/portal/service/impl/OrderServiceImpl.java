package com.cy.yunyi.portal.service.impl;

import cn.hutool.Hutool;
import cn.hutool.core.convert.Convert;
import com.alipay.api.AlipayApiException;
import com.cy.yunyi.common.service.RedisService;
import com.cy.yunyi.common.vo.PayAsyncVo;
import com.cy.yunyi.portal.component.CancelOrderSender;
import com.cy.yunyi.portal.vo.*;
import com.cy.yunyi.portal.dto.SubmitOrderDto;
import com.cy.yunyi.common.notify.NotifyService;
import com.cy.yunyi.common.notify.NotifyType;
import com.cy.yunyi.portal.service.*;
import com.cy.yunyi.common.util.AlipayUtil;
import com.cy.yunyi.portal.util.OrderHandleOption;
import com.cy.yunyi.portal.util.OrderUtil;
import com.cy.yunyi.common.exception.Asserts;
import com.cy.yunyi.common.vo.PayVo;
import com.cy.yunyi.mapper.OmsOrderMapper;
import com.cy.yunyi.mapper.UmsMemberMapper;
import com.cy.yunyi.model.*;
import com.github.pagehelper.PageHelper;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @Author: chx
 * @Description: TODO
 * @DateTime: 2021/12/30 20:08
 **/
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OmsOrderMapper orderMapper;

    @Autowired
    private UmsMemberMapper memberMapper;

    @Autowired
    private OrderGoodsService orderGoodsService;

    @Autowired
    private GoodsProductService goodsProductService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private CartService cartService;

    @Autowired
    private AlipayUtil alipayUtil;

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private RedissonClient redisson;

    @Value("${redis.database}")
    private String REDIS_DATABASE;
    @Value("${redis.key.score}")
    private String REDIS_KEY_LOCKSTOCK;

    @Autowired
    private CancelOrderSender cancelOrderSender;

    @Transactional
    @Override
    public String submit(Long userId, SubmitOrderDto submitOrderDto) {
        // ????????????
        UmsAddress checkedAddress = addressService.getAddressById(submitOrderDto.getAddressId());

        // ????????????
        Long cartId = submitOrderDto.getCartId();
        List<OmsCart> checkedGoodsList = null;
        if (cartId == 0l) {
            checkedGoodsList = cartService.getByUserIdAndChecked(userId);
        } else {
            OmsCart cart = cartService.getById(cartId);
            checkedGoodsList = new ArrayList<>(1);
            checkedGoodsList.add(cart);
        }
        if (checkedGoodsList.size() == 0) {
            Asserts.fail("??????????????????!");
        }
        BigDecimal checkedGoodsPrice = new BigDecimal(0);
        for (OmsCart checkGoods : checkedGoodsList) {
            checkedGoodsPrice = checkedGoodsPrice.add(checkGoods.getPrice().multiply(new BigDecimal(checkGoods.getNumber())));
        }

        // ??????
        BigDecimal freightPrice = new BigDecimal(0);

        // ?????????????????????????????????????????????
        BigDecimal integralPrice = new BigDecimal(0);

        // ??????????????????????????????
        BigDecimal couponPrice = new BigDecimal(0);

        // ????????????
        BigDecimal orderTotalPrice = checkedGoodsPrice.add(freightPrice).subtract(couponPrice).max(new BigDecimal(0));
        // ??????????????????
        BigDecimal actualPrice = orderTotalPrice.subtract(integralPrice);

        Long orderId = null;
        OmsOrder order = null;
        // ??????
        order = new OmsOrder();
        order.setUserId(userId);
        UmsMember member = memberMapper.selectByPrimaryKey(userId);
        order.setMemberUsername(member.getUsername());
        order.setOrderSn(this.generateOrderSn(userId));
        order.setOrderStatus(OrderUtil.STATUS_CREATE);
        order.setConsignee(checkedAddress.getName());
        order.setMobile(checkedAddress.getTel());
        order.setMessage(submitOrderDto.getMessage());
        String detailedAddress = checkedAddress.getProvince() + checkedAddress.getCity() + checkedAddress.getCounty() + " " + checkedAddress.getAddressDetail();
        order.setAddress(detailedAddress);
        order.setGoodsPrice(checkedGoodsPrice);
        order.setFreightPrice(freightPrice);
//        order.setCouponPrice(couponPrice);
//        order.setIntegralPrice(integralPrice);
        order.setOrderPrice(orderTotalPrice);
        order.setActualPrice(actualPrice);
        order.setCreateTime(new Date());
        order.setStatus(1);

        // ??????????????????
        orderMapper.insert(order);
        orderId = order.getId();

        // ????????????????????????
        for (OmsCart cartGoods : checkedGoodsList) {
            // ????????????
            OmsOrderGoods orderGoods = new OmsOrderGoods();
            orderGoods.setOrderId(order.getId());
            orderGoods.setGoodsId(cartGoods.getGoodsId());
            orderGoods.setGoodsSn(cartGoods.getGoodsSn());
            orderGoods.setProductId(cartGoods.getProductId());
            orderGoods.setGoodsName(cartGoods.getGoodsName());
            orderGoods.setIcon(cartGoods.getIcon());
            orderGoods.setPrice(cartGoods.getPrice());
            orderGoods.setNumber(cartGoods.getNumber());
            orderGoods.setSpecifications(cartGoods.getSpecifications());
            orderGoods.setCreateTime(new Date());
            orderGoods.setStatus(1);

            orderGoodsService.create(orderGoods);
        }

        // ????????????????????????????????????
        if(cartId == 0l){
            cartService.clearGoods(userId);
        }else{
            cartService.deleteById(cartId);
        }

        /**
         * ????????????????????????
         * ??????redisson??????????????????????????????
         */
        RLock rLock = redisson.getLock(REDIS_DATABASE+":"+REDIS_KEY_LOCKSTOCK);
        try {
            rLock.tryLock();
            for (OmsCart checkGoods : checkedGoodsList) {
                Long productId = checkGoods.getProductId();
                PmsGoodsProduct product = goodsProductService.getById(productId);

                int remainNumber = product.getNumber() - checkGoods.getNumber();
                if (remainNumber < 0) {
                    Asserts.fail("??????????????????????????????????????????");
                }
                if (goodsProductService.reduceStock(productId, checkGoods.getNumber()) == 0) {
                    Asserts.fail("??????????????????????????????");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //??????
            rLock.unlock();
        }
        sendDelayMessageCancelOrder(order.getId());
        return order.getOrderSn();
    }

    private void sendDelayMessageCancelOrder(Long orderId) {
        //?????????????????????????????????
        long delayTimes = 60 * 1000;
        //??????????????????
        cancelOrderSender.sendMessage(orderId, delayTimes);
    }

    /**
     *
     * @param userId   ??????ID
     * @param showType ???????????????
     *                 0??????????????????
     *                 1???????????????
     *                 2???????????????
     *                 3???????????????
     *                 4???????????????
     * @param pageSize     ????????????
     * @param pageNum     ????????????
     * @return
     */
    @Override
    public Map<String,Object> list(Long userId, Integer showType, Integer pageSize, Integer pageNum) {

        Map<String,Object> map = new HashMap<>();

        //?????????????????????????????????
        List<Integer> orderStatus = OrderUtil.orderStatus(showType);
        List<OmsOrder> orderList = queryByOrderStatus(userId, orderStatus, pageSize, pageNum);
        //??????????????????
        map.put("pageList",orderList);

        List<OrderVo> orderVoList = new ArrayList<>(orderList.size());
        for (OmsOrder order : orderList) {
            OrderVo orderVo = new OrderVo();
            orderVo.setId(order.getId());
            orderVo.setOrderSn(order.getOrderSn());
            orderVo.setActualPrice(order.getActualPrice());
            orderVo.setOrderStatusText(OrderUtil.orderStatusText(order));
            orderVo.setHandleOption(OrderUtil.build(order));
            orderVo.setAftersaleStatus(order.getAftersaleStatus());

            List<OmsOrderGoods> orderGoodsList = orderGoodsService.queryByOrderId(order.getId());
            List<OrderGoodsVo> orderGoodsVoList = new ArrayList<>(orderGoodsList.size());
            for (OmsOrderGoods orderGoods : orderGoodsList) {
                OrderGoodsVo orderGoodsVo = new OrderGoodsVo();
                orderGoodsVo.setId(orderGoods.getId());
                orderGoodsVo.setGoodsName(orderGoods.getGoodsName());
                orderGoodsVo.setNumber(orderGoods.getNumber());
                orderGoodsVo.setPicUrl(orderGoods.getIcon());
                orderGoodsVo.setSpecifications(orderGoods.getSpecifications());
                orderGoodsVo.setPrice(orderGoods.getPrice());
                orderGoodsVoList.add(orderGoodsVo);
            }
            orderVo.setOrderGoodsVoList(orderGoodsVoList);

            orderVoList.add(orderVo);
        }
        map.put("list",orderVoList);

        return map;
    }

    @Override
    public Map<String,Object> detail(Long userId, Long orderId) {
        OmsOrderExample example = new OmsOrderExample();
        OmsOrderExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(orderId);
        criteria.andUserIdEqualTo(userId);
        List<OmsOrder> orderList = orderMapper.selectByExample(example);
        if (orderList.size() == 0){
            return null;
        }
        OmsOrder order = orderList.get(0);
        OrderDetailVo orderDetailVo = new OrderDetailVo();
        BeanUtils.copyProperties(order,orderDetailVo);
        orderDetailVo.setCouponPrice(new BigDecimal("0"));
        orderDetailVo.setOrderStatusText(OrderUtil.orderStatusText(order));
        orderDetailVo.setHandleOption(OrderUtil.build(order));
        orderDetailVo.setAftersaleStatus(order.getAftersaleStatus());
        orderDetailVo.setExpCode(order.getShipChannel());
        orderDetailVo.setExpName("");
        orderDetailVo.setExpNo(order.getShipSn());

        List<OmsOrderGoods> orderGoodsList = orderGoodsService.queryByOrderId(orderId);

        Map<String, Object> result = new HashMap<>();
        result.put("orderInfo", orderDetailVo);
        result.put("orderGoods", orderGoodsList);

        return result;
    }

    //TODO ???????????????????????????
    @Override
    public Integer cancel(Long userId, Long orderId) {
        OmsOrder order = orderMapper.selectByPrimaryKey(orderId);

        //?????????????????????-1
        if (!order.getUserId().equals(userId)){
            return -1;
        }

        //????????????
        OrderHandleOption handleOption = OrderUtil.build(order);
        if (!handleOption.isCancel()) {
            return -1;
        }
        //?????????????????????????????????
        order.setOrderStatus(OrderUtil.STATUS_CANCEL);
        order.setUpdateTime(new Date());
        int result = orderMapper.updateByPrimaryKeySelective(order);
        return result;
    }

    @Override
    public Integer cancel(Long orderId) {
        OmsOrder order = orderMapper.selectByPrimaryKey(orderId);

        //????????????
        OrderHandleOption handleOption = OrderUtil.build(order);
        if (!handleOption.isCancel()) {
            return -1;
        }
        //?????????????????????????????????
        order.setOrderStatus(OrderUtil.STATUS_AUTO_CANCEL);
        order.setUpdateTime(new Date());
        int result = orderMapper.updateByPrimaryKeySelective(order);
        return result;
    }

    /**
     * ???????????????
     * @param orderSn
     * @return
     * @throws AlipayApiException
     */
    @Override
    public String aliPay(String orderSn) throws AlipayApiException {
        //??????????????????
        OmsOrderExample example = new OmsOrderExample();
        OmsOrderExample.Criteria criteria = example.createCriteria();
        criteria.andOrderSnEqualTo(orderSn);
        criteria.andOrderStatusEqualTo(101);
        criteria.andStatusEqualTo(1);
        List<OmsOrder> orderList = orderMapper.selectByExample(example);
        if (orderList.size() <= 0){
            return "?????????????????????: "+orderSn+"?????????????????????";
        }
        OmsOrder order = orderList.get(0);
        PayVo payVo = new PayVo();
        payVo.setOutTradeNo(order.getOrderSn());
        payVo.setTotalAmount(String.valueOf(order.getActualPrice()));
        payVo.setSubject("??????GO-"+order.getOrderSn());
        payVo.setBody("????????????: " + order.getOrderSn());
        String result = alipayUtil.pay(payVo);
        return result;
    }

    /**
     * ????????????
     * @param vo
     * @return
     */
    @Override
    public Integer payNotify(PayAsyncVo vo) {
        //????????????
        OmsOrderExample example = new OmsOrderExample();
        OmsOrderExample.Criteria criteria = example.createCriteria();
        criteria.andOrderSnEqualTo(vo.getOut_trade_no());
        List<OmsOrder> orderList = orderMapper.selectByExample(example);
        if (orderList.size() > 0){
            //??????????????????
            OmsOrder order = orderList.get(0);
            order.setOrderStatus(OrderUtil.STATUS_PAY);
            order.setPayId(vo.getTrade_no());
            order.setPayTime(Convert.toDate(vo.getGmt_payment()));
            int count = orderMapper.updateByPrimaryKey(order);

            //????????????????????????
            //????????????????????????????????????????????????????????????6???
            notifyService.notifySmsTemplate(order.getMobile(), NotifyType.PAY_SUCCEED,
                    new String[]{order.getOrderSn().substring(order.getOrderSn().length() - 6,order.getOrderSn().length())});

            //???????????????????????????
            notifyService.notifyMail("???????????????", order.toString());

            return count;
        }
        return 0;
    }

    //TODO ???????????????????????????
    @Override
    public Integer confirm(Long userId, Long orderId) {
        OmsOrder order = orderMapper.selectByPrimaryKey(orderId);

        //?????????????????????-1
        if (!order.getUserId().equals(userId)){
            return -1;
        }

        //????????????
        OrderHandleOption handleOption = OrderUtil.build(order);
        if (!handleOption.isConfirm()) {
            return -1;
        }
        //??????????????????????????????
        order.setOrderStatus(OrderUtil.STATUS_CONFIRM);
        order.setUpdateTime(new Date());
        int result = orderMapper.updateByPrimaryKeySelective(order);
        return result;
    }

    @Override
    public Integer refund(Long userId, Long orderId) {
        OmsOrder order = orderMapper.selectByPrimaryKey(orderId);

        //?????????????????????-1
        if (!order.getUserId().equals(userId)){
            return -1;
        }

        //????????????
        OrderHandleOption handleOption = OrderUtil.build(order);
        if (!handleOption.isRefund()) {
            return -1;
        }
        //?????????????????????????????????
        order.setOrderStatus(OrderUtil.STATUS_REFUND);
        order.setUpdateTime(new Date());
        int result = orderMapper.updateByPrimaryKeySelective(order);
        return result;
    }

    @Override
    public List<OmsOrder> queryByUserId(Long userId) {
        OmsOrderExample example = new OmsOrderExample();
        OmsOrderExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andStatusEqualTo(1);
        List<OmsOrder> orderList = orderMapper.selectByExample(example);
        return orderList;
    }


    /**
     * ??????????????????????????????
     * @param userId
     * @param orderStatus
     * @param pageSize
     * @param pageNum
     * @return
     */
    private List<OmsOrder> queryByOrderStatus(Long userId, List<Integer> orderStatus, Integer pageSize, Integer pageNum) {

        PageHelper.startPage(pageNum,pageSize);

        OmsOrderExample example = new OmsOrderExample();
        OmsOrderExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        if (orderStatus != null){
            criteria.andOrderStatusIn(orderStatus);
        }
        criteria.andStatusEqualTo(1);
        //????????????????????????
        example.setOrderByClause("create_time desc");
        List<OmsOrder> orderList = orderMapper.selectByExample(example);
        return orderList;
    }

    // TODO ?????????????????????????????????????????????????????????????????????????????????????????????????????????
    public String generateOrderSn(Long userId) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
        String now = df.format(LocalDate.now());
        String orderSn = now + getRandomNum(6);
        while (countByOrderSn(userId, orderSn) != 0) {
            orderSn = now + getRandomNum(6);
        }
        return orderSn;
    }

    private String getRandomNum(Integer num) {
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < num; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public int countByOrderSn(Long userId, String orderSn) {
        OmsOrderExample example = new OmsOrderExample();
        example.or().andUserIdEqualTo(userId).andOrderSnEqualTo(orderSn).andStatusEqualTo(1);
        return (int) orderMapper.countByExample(example);
    }

    /**
     * ?????????
     * @param userId
     * @param orderId
     * @return orderSn
     */
    @Override
    public String prepay(Long userId, Long orderId) {
        OmsOrder order = orderMapper.selectByPrimaryKey(orderId);

        //?????????????????????-1
        if (!order.getUserId().equals(userId)){
            return null;
        }

        //????????????
        OrderHandleOption handleOption = OrderUtil.build(order);
        if (!handleOption.isPay()) {
            return null;
        }
        return order.getOrderSn();
    }

    /**
     * ????????????
     * @param userId
     * @param orderId
     * @return
     */
    @Override
    public Integer delete(Long userId, Long orderId) {
        OmsOrder order = orderMapper.selectByPrimaryKey(orderId);

        //?????????????????????-1
        if (!order.getUserId().equals(userId)){
            return -1;
        }

        //????????????
        OrderHandleOption handleOption = OrderUtil.build(order);
        if (!handleOption.isDelete()) {
            return -1;
        }
        //????????????
        order.setStatus(0);
        order.setUpdateTime(new Date());
        int result = orderMapper.updateByPrimaryKeySelective(order);
        return result;
    }
}
