package com.cy.portal.service.impl;

import com.cy.portal.dto.SubmitOrderDto;
import com.cy.portal.service.*;
import com.cy.portal.util.OrderUtil;
import com.cy.yunyi.common.exception.Asserts;
import com.cy.yunyi.mapper.OmsOrderGoodsMapper;
import com.cy.yunyi.mapper.OmsOrderMapper;
import com.cy.yunyi.mapper.PmsGoodsProductMapper;
import com.cy.yunyi.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
    private OmsOrderGoodsMapper orderGoodsMapper;

    @Autowired
    private GoodsProductService goodsProductService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private CartService cartService;


    @Override
    public void submit(Long userId, SubmitOrderDto submitOrderDto) {
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

            orderGoodsMapper.insert(orderGoods);
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
