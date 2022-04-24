package com.cy.yunyi.admin.service.impl;

import com.cy.yunyi.admin.dao.IndexMapper;
import com.cy.yunyi.admin.service.IndexService;
import com.cy.yunyi.admin.vo.IndexVo;
import com.cy.yunyi.admin.vo.LineChartDataVo;
import com.cy.yunyi.mapper.OmsOrderMapper;
import com.cy.yunyi.mapper.PmsGoodsMapper;
import com.cy.yunyi.mapper.UmsMemberMapper;
import com.cy.yunyi.model.OmsOrder;
import com.cy.yunyi.model.PmsGoods;
import com.cy.yunyi.model.UmsMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author caihx
 * @Description:
 * @Date 2022/4/22
 */
@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private IndexMapper indexMapper;

    @Override
    public IndexVo index() {
        IndexVo indexVo = new IndexVo();
        return indexVo;
    }

    @Override
    public LineChartDataVo getLineChartData() {
        LineChartDataVo lineChartDataVo = new LineChartDataVo();
        return lineChartDataVo;
    }
}
