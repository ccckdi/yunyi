package com.cy.portal.service.impl;

import com.cy.portal.service.CartService;
import com.cy.portal.vo.CartContentVo;
import com.cy.portal.vo.CartTotalVo;
import com.cy.yunyi.mapper.OmsCartMapper;
import com.cy.yunyi.mapper.PmsGoodsMapper;
import com.cy.yunyi.mapper.PmsGoodsProductMapper;
import com.cy.yunyi.model.OmsCart;
import com.cy.yunyi.model.OmsCartExample;
import com.cy.yunyi.model.PmsGoods;
import com.cy.yunyi.model.PmsGoodsProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author: chx
 * @Description: 购物车Service实现类
 * @DateTime: 2021/12/17 0:26
 **/
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private OmsCartMapper cartMapper;

    @Autowired
    private PmsGoodsMapper goodsMapper;

    @Autowired
    private PmsGoodsProductMapper productMapper;

    @Override
    public List<OmsCart> listCart(Long userId) {
        OmsCartExample example = new OmsCartExample();
        example.createCriteria().andUserIdEqualTo(userId).andStatusEqualTo(1);
        //按照添加时间倒序
        example.setOrderByClause("create_time desc");
        List<OmsCart> cartList = cartMapper.selectByExample(example);
        return cartList;
    }

    @Override
    public Long getGoodsCount(Long userId) {
        OmsCartExample example = new OmsCartExample();
        example.createCriteria().andUserIdEqualTo(userId).andStatusEqualTo(1);
        return cartMapper.countByExample(example);
    }

    @Override
    public Long addCart(Long userId, OmsCart cart) {

        Long productId = cart.getProductId();

        //判断购物车中是否存在该产品
        OmsCartExample example = new OmsCartExample();
        example.createCriteria()
                .andUserIdEqualTo(userId)
                .andProductIdEqualTo(productId)
                .andStatusEqualTo(1);
        List<OmsCart> cartList = cartMapper.selectByExample(example);

        if (cartList != null && cartList.size() > 0){
            OmsCart oldCart = cartList.get(0);
            //已存在购物车中,修改数量
            cart.setId(oldCart.getId());
            cart.setNumber(oldCart.getNumber() + cart.getNumber());
            cart.setUpdateTime(new Date());
            cartMapper.updateByPrimaryKeySelective(cart);
        }else {
            PmsGoods goods = goodsMapper.selectByPrimaryKey(cart.getGoodsId());
            PmsGoodsProduct product = productMapper.selectByPrimaryKey(cart.getProductId());
            cart.setUserId(userId);
            cart.setGoodsSn(goods.getGoodsSn());
            cart.setGoodsName(goods.getName());
            cart.setPrice(product.getPrice());
            cart.setSpecifications(product.getSpecifications());
            cart.setChecked(0);
            cart.setIcon(product.getIcon());
            cart.setCreateTime(new Date());
            cart.setStatus(1);
            cartMapper.insert(cart);
        }

        Long goodsCount = this.getGoodsCount(userId);

        return goodsCount;
    }

    @Override
    public CartContentVo content(Long userId) {
        CartContentVo cartContentVo = new CartContentVo();
        List<OmsCart> cartList = listCart(userId);

        cartContentVo.setCartList(cartList);

        Integer goodsCount = 0;
        BigDecimal goodsAmount = new BigDecimal(0.00);
        Integer checkedGoodsCount = 0;
        BigDecimal checkedGoodsAmount = new BigDecimal(0.00);

        for (OmsCart cart : cartList) {
            goodsCount += cart.getNumber();
            goodsAmount = goodsAmount.add(cart.getPrice().multiply(new BigDecimal(cart.getNumber())));
            if (cart.getChecked() == 1) {
                checkedGoodsCount += cart.getNumber();
                checkedGoodsAmount = checkedGoodsAmount.add(cart.getPrice().multiply(new BigDecimal(cart.getNumber())));
            }
        }

        CartTotalVo cartTotalVo = new CartTotalVo();

        cartTotalVo.setGoodsCount(goodsCount);
        cartTotalVo.setGoodsAmount(goodsAmount);
        cartTotalVo.setCheckedGoodsCount(checkedGoodsCount);
        cartTotalVo.setCheckedGoodsAmount(checkedGoodsAmount);

        cartContentVo.setCartTotal(cartTotalVo);

        return cartContentVo;
    }

    @Override
    public Integer checked(Long userId, List<Long> cartId, Integer isChecked) {
        OmsCart cart = new OmsCart();
        cart.setChecked(isChecked);
        cart.setUpdateTime(new Date());

        OmsCartExample example = new OmsCartExample();
        OmsCartExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andProductIdIn(cartId);
        criteria.andStatusEqualTo(1);

        int count = cartMapper.updateByExampleSelective(cart,example);
        return count;
    }

    @Override
    public List<OmsCart> getByUserIdAndChecked(Long userId) {
        OmsCartExample example = new OmsCartExample();
        OmsCartExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andCheckedEqualTo(1);
        criteria.andStatusEqualTo(1);
        List<OmsCart> cartList = cartMapper.selectByExample(example);
        return cartList;
    }

    @Override
    public OmsCart getById(Long cartId) {
        OmsCartExample example = new OmsCartExample();
        OmsCartExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(cartId);
        criteria.andStatusEqualTo(1);
        List<OmsCart> cartList = cartMapper.selectByExample(example);
        if (cartList.size() > 0){
            return cartList.get(0);
        }
        return null;
    }

    @Override
    public void clearGoods(Long userId) {
        OmsCartExample example = new OmsCartExample();
        OmsCartExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andCheckedEqualTo(1);
        criteria.andStatusEqualTo(1);
        OmsCart cart = new OmsCart();
        cart.setStatus(0);
        cartMapper.updateByExampleSelective(cart,example);
    }

    @Override
    public void deleteById(Long cartId) {
        OmsCart cart = new OmsCart();
        cart.setId(cartId);
        cart.setStatus(0);
        cartMapper.updateByPrimaryKeySelective(cart);
    }
}
