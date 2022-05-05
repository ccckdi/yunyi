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

import java.math.BigDecimal;
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
    @Autowired
    private PmsGoodsMapper goodsMapper;
    @Autowired
    private UmsMemberMapper memberMapper;

    @Override
    public IndexVo index() {
        IndexVo indexVo = new IndexVo();
        OmsOrderExample orderExample = new OmsOrderExample();
//        PmsGoodsExample goodsExample = new PmsGoodsExample();
//        UmsMemberExample memberExample = new UmsMemberExample();
        OmsOrderExample.Criteria orderCriteria = orderExample.or();
//        PmsGoodsExample.Criteria goodsCriteria = goodsExample.or();
//        UmsMemberExample.Criteria memberCriteria = memberExample.or();

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());//设置起时间
        //将小时置0
        cal.set(Calendar.HOUR_OF_DAY, 0);
        //将分钟置0
        cal.set(Calendar.MINUTE, 0);
        //将秒置0
        cal.set(Calendar.SECOND,0);
        //将毫秒置0
        cal.set(Calendar.MILLISECOND, 0);
        Date startTime = cal.getTime();

        //将小时置23
        cal.set(Calendar.HOUR_OF_DAY, 23);
        //将分钟置59
        cal.set(Calendar.MINUTE, 59);
        //将秒置59
        cal.set(Calendar.SECOND,59);
        //将毫秒置59
        cal.set(Calendar.MILLISECOND, 59);
        Date endTime = cal.getTime();

        orderCriteria.andCreateTimeBetween(startTime,endTime);
//        goodsCriteria.andCreateTimeBetween(startTime,endTime);
//        memberCriteria.andCreateTimeBetween(startTime,endTime);
        orderCriteria.andStatusEqualTo(1);
//        goodsCriteria.andStatusEqualTo(1);
//        memberCriteria.andStatusEqualTo(1);

        List<OmsOrder> todayOrderList = orderMapper.selectByExample(orderExample);

        cal.add(Calendar.DATE, -1);//减去一天
        endTime = cal.getTime();
        //将小时置0
        cal.set(Calendar.HOUR_OF_DAY, 0);
        //将分钟置0
        cal.set(Calendar.MINUTE, 0);
        //将秒置0
        cal.set(Calendar.SECOND,0);
        //将毫秒置0
        cal.set(Calendar.MILLISECOND, 0);
        startTime = cal.getTime();

        OmsOrderExample yesterdayOrderExample = new OmsOrderExample();
        yesterdayOrderExample.createCriteria().andCreateTimeBetween(startTime,endTime).andStatusEqualTo(1);
        List<OmsOrder> yesterdayOrderList = orderMapper.selectByExample(yesterdayOrderExample);

//        List<PmsGoods> todayGoodsList = goodsMapper.selectByExample(goodsExample);
//        List<UmsMember> todayMemberList = memberMapper.selectByExample(memberExample);

        Integer totalOrdersToday = todayOrderList.size();
        BigDecimal totalSalesToday = new BigDecimal("0");
        for (OmsOrder order : todayOrderList) {
            totalSalesToday = totalSalesToday.add(order.getOrderPrice());
        }
        BigDecimal totalSalesYesterday = new BigDecimal("0");
        for (OmsOrder order : yesterdayOrderList) {
            totalSalesYesterday = totalSalesYesterday.add(order.getOrderPrice());
        }

        indexVo.setTotalOrdersToday(totalOrdersToday);
        indexVo.setTotalSalesToday(totalSalesToday);
        indexVo.setTotalSalesYesterday(totalSalesYesterday);

        return indexVo;
    }

    @Override
    public LineChartDataVo getLineChartData() {
        LineChartDataVo lineChartDataVo = new LineChartDataVo();
        return lineChartDataVo;
    }
}
