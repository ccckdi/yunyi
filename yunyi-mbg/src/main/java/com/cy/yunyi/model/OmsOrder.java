package com.cy.yunyi.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OmsOrder implements Serializable {
    private Long id;

    @ApiModelProperty(value = "用户表的用户ID")
    private Long userId;

    @ApiModelProperty(value = "订单编号")
    private String orderSn;

    @ApiModelProperty(value = "订单状态")
    private Integer orderStatus;

    @ApiModelProperty(value = "售后状态，0是可申请，1是用户已申请，2是管理员审核通过，3是管理员退款成功，4是管理员审核拒绝，5是用户已取消")
    private Integer aftersaleStatus;

    @ApiModelProperty(value = "收货人名称")
    private String consignee;

    @ApiModelProperty(value = "收货人手机号")
    private String mobile;

    @ApiModelProperty(value = "收货具体地址")
    private String address;

    @ApiModelProperty(value = "备注")
    private String message;

    @ApiModelProperty(value = "商品总费用")
    private BigDecimal goodsPrice;

    @ApiModelProperty(value = "配送费用")
    private BigDecimal freightPrice;

    @ApiModelProperty(value = "订单费用， = goods_price + freight_price - coupon_price")
    private BigDecimal orderPrice;

    @ApiModelProperty(value = "实付费用， = order_price - integral_price")
    private BigDecimal actualPrice;

    @ApiModelProperty(value = "微信付款编号")
    private String payId;

    @ApiModelProperty(value = "微信付款时间")
    private Date payTime;

    @ApiModelProperty(value = "发货编号")
    private String shipSn;

    @ApiModelProperty(value = "发货快递公司")
    private String shipChannel;

    @ApiModelProperty(value = "发货开始时间")
    private Date shipTime;

    @ApiModelProperty(value = "实际退款金额，（有可能退款金额小于实际支付金额）")
    private BigDecimal refundAmount;

    @ApiModelProperty(value = "退款方式")
    private String refundType;

    @ApiModelProperty(value = "退款备注")
    private String refundContent;

    @ApiModelProperty(value = "退款时间")
    private Date refundTime;

    @ApiModelProperty(value = "用户确认收货时间")
    private Date confirmTime;

    @ApiModelProperty(value = "订单关闭时间")
    private Date endTime;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "启用状态：0->禁用；1->启用")
    private Integer status;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getAftersaleStatus() {
        return aftersaleStatus;
    }

    public void setAftersaleStatus(Integer aftersaleStatus) {
        this.aftersaleStatus = aftersaleStatus;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public BigDecimal getFreightPrice() {
        return freightPrice;
    }

    public void setFreightPrice(BigDecimal freightPrice) {
        this.freightPrice = freightPrice;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public BigDecimal getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(BigDecimal actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getShipSn() {
        return shipSn;
    }

    public void setShipSn(String shipSn) {
        this.shipSn = shipSn;
    }

    public String getShipChannel() {
        return shipChannel;
    }

    public void setShipChannel(String shipChannel) {
        this.shipChannel = shipChannel;
    }

    public Date getShipTime() {
        return shipTime;
    }

    public void setShipTime(Date shipTime) {
        this.shipTime = shipTime;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getRefundType() {
        return refundType;
    }

    public void setRefundType(String refundType) {
        this.refundType = refundType;
    }

    public String getRefundContent() {
        return refundContent;
    }

    public void setRefundContent(String refundContent) {
        this.refundContent = refundContent;
    }

    public Date getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", orderSn=").append(orderSn);
        sb.append(", orderStatus=").append(orderStatus);
        sb.append(", aftersaleStatus=").append(aftersaleStatus);
        sb.append(", consignee=").append(consignee);
        sb.append(", mobile=").append(mobile);
        sb.append(", address=").append(address);
        sb.append(", message=").append(message);
        sb.append(", goodsPrice=").append(goodsPrice);
        sb.append(", freightPrice=").append(freightPrice);
        sb.append(", orderPrice=").append(orderPrice);
        sb.append(", actualPrice=").append(actualPrice);
        sb.append(", payId=").append(payId);
        sb.append(", payTime=").append(payTime);
        sb.append(", shipSn=").append(shipSn);
        sb.append(", shipChannel=").append(shipChannel);
        sb.append(", shipTime=").append(shipTime);
        sb.append(", refundAmount=").append(refundAmount);
        sb.append(", refundType=").append(refundType);
        sb.append(", refundContent=").append(refundContent);
        sb.append(", refundTime=").append(refundTime);
        sb.append(", confirmTime=").append(confirmTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", status=").append(status);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}