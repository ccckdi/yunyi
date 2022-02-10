package com.cy.portal.service.impl;

import com.cy.portal.service.FootprintService;
import com.cy.yunyi.mapper.RmsFootprintMapper;
import com.cy.yunyi.model.RmsFootprint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author caihx
 * @Description: 用户足迹Service实现类
 * @Date 2022/2/9
 */
@Service
public class FootprintServiceImpl implements FootprintService {

    @Autowired
    private RmsFootprintMapper footprintMapper;

    @Override
    public void create(RmsFootprint footprint) {
        footprint.setCreateTime(new Date());
        footprint.setStatus(1);
        footprintMapper.insert(footprint);
    }
}
