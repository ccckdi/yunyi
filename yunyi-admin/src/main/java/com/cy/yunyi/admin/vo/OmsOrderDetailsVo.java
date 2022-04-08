package com.cy.yunyi.admin.vo;

import com.cy.yunyi.model.OmsOrderGoods;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Author caihx
 * @Description: 订单详情
 * @Date 2022/4/8
 */
@Data
public class OmsOrderDetailsVo extends OmsOrderVo{
    @ApiModelProperty("订单商品列表")
    private List<OmsOrderGoods> omsOrderGoodsList;
//    @ApiModelProperty("订单操作记录列表")
//    private List<OmsOrderOperateHistory> historyList;
}
