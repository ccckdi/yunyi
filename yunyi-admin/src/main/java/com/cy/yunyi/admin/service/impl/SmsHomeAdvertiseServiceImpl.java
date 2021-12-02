package com.cy.yunyi.admin.service.impl;

import com.cy.yunyi.admin.service.SmsHomeAdvertiseService;
import com.cy.yunyi.admin.service.SmsHomeAdvertiseService;
import com.cy.yunyi.mapper.SmsHomeAdvertiseMapper;
import com.cy.yunyi.model.SmsHomeAdvertise;
import com.cy.yunyi.model.SmsHomeAdvertiseExample;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        int count = advertiseMapper.updateByPrimaryKey(brand);
        return count;
    }

    @Override
    public List<SmsHomeAdvertise> list(String keyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        SmsHomeAdvertiseExample example = new SmsHomeAdvertiseExample();
        example.createCriteria().andNameEqualTo("%" + keyword + "%");
        List<SmsHomeAdvertise> brandList = advertiseMapper.selectByExample(example);
        return brandList;
    }

    @Override
    public int delete(Long id) {
        int count = advertiseMapper.deleteByPrimaryKey(id);
        return count;
    }
}
