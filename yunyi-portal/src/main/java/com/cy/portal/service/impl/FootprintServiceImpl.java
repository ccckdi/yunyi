package com.cy.portal.service.impl;

import com.cy.portal.service.FootprintService;
import com.cy.yunyi.mapper.RmsFootprintMapper;
import com.cy.yunyi.model.RmsFootprint;
import com.cy.yunyi.model.RmsFootprintExample;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

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

    @Override
    public List<RmsFootprint> list(Long userId, Integer pageNum, Integer pageSize, String sort, String order) {
        PageHelper.startPage(pageNum,pageSize);
        RmsFootprintExample example = new RmsFootprintExample();
        RmsFootprintExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andStatusEqualTo(1);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        List<RmsFootprint> footprintList = footprintMapper.selectByExample(example);
        return footprintList;
    }

    @Override
    public List<RmsFootprint> queryByUserId(Long userId) {
        RmsFootprintExample example = new RmsFootprintExample();
        RmsFootprintExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andStatusEqualTo(1);
        List<RmsFootprint> footprintList = footprintMapper.selectByExample(example);
        return footprintList;
    }
}
