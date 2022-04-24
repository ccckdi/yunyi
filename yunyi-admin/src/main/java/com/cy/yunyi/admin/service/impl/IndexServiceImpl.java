package com.cy.yunyi.admin.service.impl;

import com.cy.yunyi.admin.service.IndexService;
import com.cy.yunyi.admin.vo.IndexVo;
import com.cy.yunyi.admin.vo.LineChartDataVo;
import org.springframework.stereotype.Service;

/**
 * @Author caihx
 * @Description:
 * @Date 2022/4/22
 */
@Service
public class IndexServiceImpl implements IndexService {
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
