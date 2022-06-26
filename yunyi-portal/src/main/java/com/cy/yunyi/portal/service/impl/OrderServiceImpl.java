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
        // 收货地址
        UmsAddress checkedAddress = addressService.getAddressById(submitOrderDto.getAddressId());

        // 货品价格
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
            Asserts.fail("购物车已清空!");
        }
        BigDecimal checkedGoodsPrice = new BigDecimal(0);
        for (OmsCart checkGoods : checkedGoodsList) {
            checkedGoodsPrice = checkedGoodsPrice.add(checkGoods.getPrice().multiply(new BigDecimal(checkGoods.getNumber())));
        }

        // 运费
        BigDecimal freightPrice = new BigDecimal(0);

        // 可以使用的其他钱，例如用户积分
        BigDecimal integralPrice = new BigDecimal(0);

        // 使用优惠券减免的金额
        BigDecimal couponPrice = new BigDecimal(0);

        // 订单费用
        BigDecimal orderTotalPrice = checkedGoodsPrice.add(freightPrice).subtract(couponPrice).max(new BigDecimal(0));
        // 最终支付费用
        BigDecimal actualPrice = orderTotalPrice.subtract(integralPrice);

        Long orderId = null;
        OmsOrder order = null;
        // 订单
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

        // 添加订单表项
        orderMapper.insert(order);
        orderId = order.getId();

        // 添加订单商品表项
        for (OmsCart cartGoods : checkedGoodsList) {
            // 订单商品
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

        // 删除购物车里面的商品信息
        if(cartId == 0l){
            cartService.clearGoods(userId);
        }else{
            cartService.deleteById(cartId);
        }

        /**
         * 商品货品数量减少
         * 使用redisson分布式锁解决超卖问题
         */
        RLock rLock = redisson.getLock(REDIS_DATABASE+":"+REDIS_KEY_LOCKSTOCK);
        try {
            rLock.tryLock();
            for (OmsCart checkGoods : checkedGoodsList) {
                Long productId = checkGoods.getProductId();
                PmsGoodsProduct product = goodsProductService.getById(productId);

                int remainNumber = product.getNumber() - checkGoods.getNumber();
                if (remainNumber < 0) {
                    Asserts.fail("下单的商品货品数量大于库存量");
                }
                if (goodsProductService.reduceStock(productId, checkGoods.getNumber()) == 0) {
                    Asserts.fail("商品货品库存减少失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //解锁
            rLock.unlock();
        }
        sendDelayMessageCancelOrder(order.getId());
        return order.getOrderSn();
    }

    private void sendDelayMessageCancelOrder(Long orderId) {
        //获取订单超时时间，毫秒
        long delayTimes = 60 * 1000;
        //发送延迟消息
        cancelOrderSender.sendMessage(orderId, delayTimes);
    }

    /**
     *
     * @param userId   用户ID
     * @param showType 订单信息：
     *                 0，全部订单；
     *                 1，待付款；
     *                 2，待发货；
     *                 3，待收货；
     *                 4，待评论。
     * @param pageSize     分页页数
     * @param pageNum     分页大小
     * @return
     */
    @Override
    public Map<String,Object> list(Long userId, Integer showType, Integer pageSize, Integer pageNum) {

        Map<String,Object> map = new HashMap<>();

        //获取对应对应的订单状态
        List<Integer> orderStatus = OrderUtil.orderStatus(showType);
        List<OmsOrder> orderList = queryByOrderStatus(userId, orderStatus, pageSize, pageNum);
        //用于分页处理
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

    //TODO 需校验最后更新时间
    @Override
    public Integer cancel(Long userId, Long orderId) {
        OmsOrder order = orderMapper.selectByPrimaryKey(orderId);

        //非本人操作返回-1
        if (!order.getUserId().equals(userId)){
            return -1;
        }

        //校验状态
        OrderHandleOption handleOption = OrderUtil.build(order);
        if (!handleOption.isCancel()) {
            return -1;
        }
        //订单状态修改为用户取消
        order.setOrderStatus(OrderUtil.STATUS_CANCEL);
        order.setUpdateTime(new Date());
        int result = orderMapper.updateByPrimaryKeySelective(order);
        return result;
    }

    @Override
    public Integer cancel(Long orderId) {
        OmsOrder order = orderMapper.selectByPrimaryKey(orderId);

        //校验状态
        OrderHandleOption handleOption = OrderUtil.build(order);
        if (!handleOption.isCancel()) {
            return -1;
        }
        //订单状态修改为用户取消
        order.setOrderStatus(OrderUtil.STATUS_AUTO_CANCEL);
        order.setUpdateTime(new Date());
        int result = orderMapper.updateByPrimaryKeySelective(order);
        return result;
    }

    /**
     * 支付宝支付
     * @param orderSn
     * @return
     * @throws AlipayApiException
     */
    @Override
    public String aliPay(String orderSn) throws AlipayApiException {
        //查找对应订单
        OmsOrderExample example = new OmsOrderExample();
        OmsOrderExample.Criteria criteria = example.createCriteria();
        criteria.andOrderSnEqualTo(orderSn);
        criteria.andOrderStatusEqualTo(101);
        criteria.andStatusEqualTo(1);
        List<OmsOrder> orderList = orderMapper.selectByExample(example);
        if (orderList.size() <= 0){
            return "不存在订单编号: "+orderSn+"的未支付订单！";
        }
        OmsOrder order = orderList.get(0);
        PayVo payVo = new PayVo();
        payVo.setOutTradeNo(order.getOrderSn());
        payVo.setTotalAmount(String.valueOf(order.getActualPrice()));
        payVo.setSubject("云毅GO-"+order.getOrderSn());
        payVo.setBody("订单编号: " + order.getOrderSn());
        String result = alipayUtil.pay(payVo);
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
            order.setOrderStatus(OrderUtil.STATUS_PAY);
            order.setPayId(vo.getTrade_no());
            order.setPayTime(Convert.toDate(vo.getGmt_payment()));
            int count = orderMapper.updateByPrimaryKey(order);

            //异步短信通知用户
            //订单编号由于受腾讯云参数长度现在只保留后6位
            notifyService.notifySmsTemplate(order.getMobile(), NotifyType.PAY_SUCCEED,
                    new String[]{order.getOrderSn().substring(order.getOrderSn().length() - 6,order.getOrderSn().length())});

            //异步邮件通知管理员
            notifyService.notifyMail("新订单通知", order.toString());

            return count;
        }
        return 0;
    }

    //TODO 需校验最后更新时间
    @Override
    public Integer confirm(Long userId, Long orderId) {
        OmsOrder order = orderMapper.selectByPrimaryKey(orderId);

        //非本人操作返回-1
        if (!order.getUserId().equals(userId)){
            return -1;
        }

        //校验状态
        OrderHandleOption handleOption = OrderUtil.build(order);
        if (!handleOption.isConfirm()) {
            return -1;
        }
        //订单状态修改为待评价
        order.setOrderStatus(OrderUtil.STATUS_CONFIRM);
        order.setUpdateTime(new Date());
        int result = orderMapper.updateByPrimaryKeySelective(order);
        return result;
    }

    @Override
    public Integer refund(Long userId, Long orderId) {
        OmsOrder order = orderMapper.selectByPrimaryKey(orderId);

        //非本人操作返回-1
        if (!order.getUserId().equals(userId)){
            return -1;
        }

        //校验状态
        OrderHandleOption handleOption = OrderUtil.build(order);
        if (!handleOption.isRefund()) {
            return -1;
        }
        //订单状态修改为申请退款
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
     * 根据订单状态获取订单
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
        //按照下单时间倒序
        example.setOrderByClause("create_time desc");
        List<OmsOrder> orderList = orderMapper.selectByExample(example);
        return orderList;
    }

    // TODO 这里应该产生一个唯一的订单，但是实际上这里仍然存在两个订单相同的可能性
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
     * 去付款
     * @param userId
     * @param orderId
     * @return orderSn
     */
    @Override
    public String prepay(Long userId, Long orderId) {
        OmsOrder order = orderMapper.selectByPrimaryKey(orderId);

        //非本人操作返回-1
        if (!order.getUserId().equals(userId)){
            return null;
        }

        //校验状态
        OrderHandleOption handleOption = OrderUtil.build(order);
        if (!handleOption.isPay()) {
            return null;
        }
        return order.getOrderSn();
    }

    /**
     * 删除订单
     * @param userId
     * @param orderId
     * @return
     */
    @Override
    public Integer delete(Long userId, Long orderId) {
        OmsOrder order = orderMapper.selectByPrimaryKey(orderId);

        //非本人操作返回-1
        if (!order.getUserId().equals(userId)){
            return -1;
        }

        //校验状态
        OrderHandleOption handleOption = OrderUtil.build(order);
        if (!handleOption.isDelete()) {
            return -1;
        }
        //逻辑删除
        order.setStatus(0);
        order.setUpdateTime(new Date());
        int result = orderMapper.updateByPrimaryKeySelective(order);
        return result;
    }
}
