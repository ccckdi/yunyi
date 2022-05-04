package com.cy.yunyi.admin.service.impl;

import com.cy.yunyi.admin.service.IndexService;
import com.cy.yunyi.admin.vo.IndexVo;
import com.cy.yunyi.admin.vo.LineChartDataVo;
import com.cy.yunyi.mapper.OmsOrderMapper;
import com.cy.yunyi.mapper.PmsGoodsMapper;
import com.cy.yunyi.mapper.UmsMemberMapper;
import com.cy.yunyi.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author caihx
 * @Description:
 * @Date 2022/4/22
 */
@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private OmsOrderMapper orderMapper;
    private PmsGoodsMapper goodsMapper;
    private UmsMemberMapper memberMapper;

    @Override
    public IndexVo index() {
        IndexVo indexVo = new IndexVo();
        OmsOrderExample orderExample = new OmsOrderExample();
        PmsGoodsExample goodsExample = new PmsGoodsExample();
        UmsMemberExample memberExample = new UmsMemberExample();
        OmsOrderExample.Criteria orderCriteria = orderExample.or();
        PmsGoodsExample.Criteria goodsCriteria = goodsExample.or();
        UmsMemberExample.Criteria memberCriteria = memberExample.or();

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());//设置起时间
        //将小时至0
        cal.set(Calendar.HOUR_OF_DAY, 0);
        //将分钟至0
        cal.set(Calendar.MINUTE, 0);
        //将秒至0
        cal.set(Calendar.SECOND,0);
        //将毫秒至0
        cal.set(Calendar.MILLISECOND, 0);
        Date startTime = cal.getTime();
        cal.add(Calendar.DATE, 1);//增加一天
        Date endTime = cal.getTime();

        orderCriteria.andCreateTimeBetween(startTime,endTime);
        goodsCriteria.andCreateTimeBetween(startTime,endTime);
        memberCriteria.andCreateTimeBetween(startTime,endTime);
        orderCriteria.andStatusEqualTo(1);
        goodsCriteria.andStatusEqualTo(1);
        memberCriteria.andStatusEqualTo(1);

        List<OmsOrder> orderList = orderMapper.selectByExample(orderExample);
        List<PmsGoods> goodsList = goodsMapper.selectByExample(goodsExample);
        List<UmsMember> memberList = memberMapper.selectByExample(memberExample);



        return indexVo;
    }

    @Override
    public LineChartDataVo getLineChartData() {
        LineChartDataVo lineChartDataVo = new LineChartDataVo();
        return lineChartDataVo;
    }
}
