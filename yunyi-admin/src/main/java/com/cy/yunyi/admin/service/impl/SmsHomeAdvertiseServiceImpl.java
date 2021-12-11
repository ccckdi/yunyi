package com.cy.yunyi.admin.service.impl;

import com.cy.yunyi.admin.service.SmsHomeAdvertiseService;
import com.cy.yunyi.admin.service.SmsHomeAdvertiseService;
import com.cy.yunyi.mapper.SmsHomeAdvertiseMapper;
import com.cy.yunyi.model.SmsHomeAdvertise;
import com.cy.yunyi.model.SmsHomeAdvertiseExample;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author: chx
 * @Description: 商品品牌管理Service实现类
 * @DateTime: 2021/11/30 11:01
 **/
@Service
public class SmsHomeAdvertiseServiceImpl implements SmsHomeAdvertiseService {

    @Autowired
    private SmsHomeAdvertiseMapper advertiseMapper;

    @Override
    public int create(SmsHomeAdvertise brand) {
        int count = advertiseMapper.insert(brand);
        return count;
    }

    @Override
    public int update(Long id, SmsHomeAdvertise brand) {
        brand.setId(id);
        int count = advertiseMapper.updateByPrimaryKeySelective(brand);
        return count;
    }

    @Override
    public List<SmsHomeAdvertise> list(String name, Integer type, String endTime, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        SmsHomeAdvertiseExample example = new SmsHomeAdvertiseExample();
        SmsHomeAdvertiseExample.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        if (type != null) {
            criteria.andTypeEqualTo(type);
        }
        if (!StringUtils.isEmpty(endTime)) {
            String startStr = endTime + " 00:00:00";
            String endStr = endTime + " 23:59:59";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start = null;
            try {
                start = sdf.parse(startStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date end = null;
            try {
                end = sdf.parse(endStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (start != null && end != null) {
                criteria.andEndTimeBetween(start, end);
            }
        }
        example.setOrderByClause("sort desc");
        return advertiseMapper.selectByExample(example);
    }

    @Override
    public int delete(Long id) {
        int count = advertiseMapper.deleteByPrimaryKey(id);
        return count;
    }

    @Override
    public SmsHomeAdvertise info(Long id) {
        SmsHomeAdvertise smsHomeAdvertise = advertiseMapper.selectByPrimaryKey(id);
        return smsHomeAdvertise;
    }
}
