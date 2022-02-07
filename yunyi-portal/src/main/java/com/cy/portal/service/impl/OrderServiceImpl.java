package com.cy.portal.service.impl;

import com.alipay.api.AlipayApiException;
import com.cy.portal.dto.PayAsyncVo;
import com.cy.portal.dto.PayVo;
import com.cy.portal.dto.SubmitOrderDto;
import com.cy.portal.service.*;
import com.cy.portal.util.AlipayUtil;
import com.cy.portal.util.OrderUtil;
import com.cy.portal.vo.OrderGoodsVo;
import com.cy.portal.vo.OrderVo;
import com.cy.yunyi.common.exception.Asserts;
import com.cy.yunyi.mapper.OmsOrderMapper;
import com.cy.yunyi.model.*;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    private OrderGoodsService orderGoodsService;

    @Autowired
    private GoodsProductService goodsProductService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private CartService cartService;

    @Autowired
    private AlipayUtil alipayUtil;

    @Override
    public Long submit(Long userId, SubmitOrderDto submitOrderDto) {
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

            orderGoodsService.insert(orderGoods);
        }

        // 删除购物车里面的商品信息
        if(cartId == 0l){
            cartService.clearGoods(userId);
        }else{
            cartService.deleteById(cartId);
        }

        // 商品货品数量减少
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

        return orderId;
    }

    /**
     *
     * @param userId   用户ID
     * @param showType 订单信息：
     *                 0，全部订单；
     *                 1，待付款；
     *                 2，待发货；
     *                 3，待收货；
     *                 4，待评价。
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
    public String aliPay(Long orderId) throws AlipayApiException {
        //查找对应订单
        OmsOrderExample example = new OmsOrderExample();
        OmsOrderExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(orderId);
        criteria.andOrderStatusEqualTo(101);
        criteria.andStatusEqualTo(1);
        List<OmsOrder> orderList = orderMapper.selectByExample(example);
        if (orderList.size() <= 0){
            return "不存在订单号为\""+orderId+"\"的未支付订单！";
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
            order.setOrderStatus(201);
            int count = orderMapper.updateByPrimaryKey(order);

        }
        return 0;
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
}
