package com.cy.yunyi.portal.controller;

import com.cy.yunyi.portal.annotation.LoginUser;
import com.cy.yunyi.portal.service.AddressService;
import com.cy.yunyi.portal.service.CartService;
import com.cy.yunyi.portal.vo.CartContentVo;
import com.cy.yunyi.portal.vo.CheckoutOrderVo;
import com.cy.yunyi.common.api.CommonResult;
import com.cy.yunyi.common.util.JacksonUtil;
import com.cy.yunyi.model.OmsCart;
import com.cy.yunyi.model.UmsAddress;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: chx
 * @Description: 购物车管理Controller
 * @DateTime: 2021/12/13 23:30
 **/
@RestController
@Api(tags = "CartController", description = "购物车管理")
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private AddressService addressService;

    @ApiOperation("购物车展示")
    @GetMapping("/index")
        public CommonResult<CartContentVo> index(@LoginUser Long userId) {
        if (userId == null) {
            return CommonResult.unauthorized();
        }
        CartContentVo cartContentVo = cartService.content(userId);
        return CommonResult.success(cartContentVo);
    }

    @ApiOperation("加入购物车")
    @PostMapping("/add")
    public CommonResult add(@LoginUser Long userId,@RequestBody OmsCart cart) {
        if (userId == null) {
            return CommonResult.unauthorized();
        }
        Long goodsCount = cartService.addCart(userId, cart);
        return CommonResult.success(goodsCount);
    }

    @ApiOperation("购物车商品数量")
    @GetMapping("/goodscount")
    public CommonResult goodscount(@LoginUser Long userId) {
        if (userId == null) {
            return CommonResult.unauthorized();
        }
        Long goodsCount = cartService.getGoodsCount(userId);
        return CommonResult.success(goodsCount);
    }

    /**
     * @param userId 用户ID
     * @param body   购物车商品信息， { productIds: xxx, isChecked: 1/0 }
     * @return
     */
    @ApiOperation("选中商品")
    @PostMapping("/checked")
    public CommonResult checked(@LoginUser Long userId,@RequestBody String body) {
        if (userId == null) {
            return CommonResult.unauthorized();
        }
        if (body == null) {
            return CommonResult.validateFailed();
        }
        List<Long> cartIds = JacksonUtil.parseLongList(body, "cartIds");
        Integer isChecked = JacksonUtil.parseInteger(body, "isChecked");
        cartService.checked(userId, cartIds, isChecked);
        return index(userId);
    }

    @ApiOperation("确认订单")
    @GetMapping("/checkout")
    public CommonResult checkout(@LoginUser Long userId, Long cartId, Long addressId, Long couponId, Long userCouponId, Long grouponRulesId) {

//        CheckoutOrderDto checkoutOrderDto = new CheckoutOrderDto();
//
//        Long cartId = checkoutOrderDto.getCartId();
//        Long addressId = checkoutOrderDto.getAddressId();
//        Long couponId = checkoutOrderDto.getCouponId();
//        Long userCouponId = checkoutOrderDto.getUserCouponId();

        if (userId == null) {
            return CommonResult.unauthorized();
        }
        // 收货地址
        UmsAddress checkedAddress = null;
        if (addressId == null || addressId == 0l) {
            checkedAddress = addressService.findDefault(userId);
            // 如果仍然没有地址，则是没有收货地址
            // 返回一个空的地址id=0，这样前端则会提醒添加地址
            if (checkedAddress == null) {
                checkedAddress = new UmsAddress();
                checkedAddress.setId(0l);
                addressId = 0l;
            } else {
                addressId = checkedAddress.getId();
            }

        } else {
            checkedAddress = addressService.getAddressById(addressId);
            // 如果null, 则报错
            if (checkedAddress == null) {
                return CommonResult.validateFailed();
            }
        }

        // 商品价格
        List<OmsCart> checkedGoodsList = null;
        if (cartId == null || cartId == 0l) {
            checkedGoodsList = cartService.getByUserIdAndChecked(userId);
        } else {
            OmsCart cart = cartService.getById(cartId);
            if (cart == null) {
                return CommonResult.validateFailed();
            }
            checkedGoodsList = new ArrayList<>(1);
            checkedGoodsList.add(cart);
        }
        BigDecimal checkedGoodsPrice = new BigDecimal(0.00);
        for (OmsCart cart : checkedGoodsList) {
            checkedGoodsPrice = checkedGoodsPrice.add(cart.getPrice().multiply(new BigDecimal(cart.getNumber())));
        }

        // 运费
        BigDecimal freightPrice = new BigDecimal(0.00);

        // 获取优惠券减免金额，优惠券可用数量
        int availableCouponLength = 0;
        // 优惠金额
        BigDecimal couponPrice = new BigDecimal(0);

        // 订单费用
        BigDecimal orderTotalPrice = checkedGoodsPrice.add(freightPrice).subtract(couponPrice).max(new BigDecimal(0.00));

        // 可以使用的其他钱，例如用户积分
        BigDecimal integralPrice = new BigDecimal(0.00);

        BigDecimal actualPrice = orderTotalPrice.subtract(integralPrice);

        CheckoutOrderVo checkoutOrderVo = new CheckoutOrderVo();
        checkoutOrderVo.setAddressId(addressId);
        checkoutOrderVo.setCouponId(couponId);
        checkoutOrderVo.setUserCouponId(userCouponId);
        checkoutOrderVo.setCartId(cartId);
        checkoutOrderVo.setCheckedAddress(checkedAddress);
        checkoutOrderVo.setAvailableCouponLength(availableCouponLength);
        checkoutOrderVo.setGoodsTotalPrice(checkedGoodsPrice);
        checkoutOrderVo.setFreightPrice(freightPrice);
        checkoutOrderVo.setCouponPrice(couponPrice);
        checkoutOrderVo.setOrderTotalPrice(orderTotalPrice);
        checkoutOrderVo.setActualPrice(actualPrice);
        checkoutOrderVo.setCheckedGoodsList(checkedGoodsList);

        return CommonResult.success(checkoutOrderVo);
    }

    @ApiOperation("移除购物车")
    @PostMapping("/delete")
    public CommonResult delete(@LoginUser Long userId,@RequestBody String body) {
        if (userId == null) {
            return CommonResult.unauthorized();
        }
        if (body == null) {
            return CommonResult.validateFailed();
        }
        List<Long> productIds = JacksonUtil.parseLongList(body, "productIds");
        cartService.deleteCart(userId,productIds);
        CartContentVo cartContentVo = cartService.content(userId);
        return CommonResult.success(cartContentVo);
    }
}
