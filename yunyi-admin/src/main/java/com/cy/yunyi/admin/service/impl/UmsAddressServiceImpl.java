package com.cy.yunyi.admin.service.impl;

import com.cy.yunyi.admin.service.UmsAddressService;
import com.cy.yunyi.mapper.UmsAddressMapper;
import com.cy.yunyi.model.UmsAddress;
import com.cy.yunyi.model.UmsAddressExample;
import com.cy.yunyi.model.UmsAddress;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author: chx
 * @Description: 收货地址Service实现类
 * @DateTime: 2021/12/2 9:58
 **/
public class UmsAddressServiceImpl implements UmsAddressService {

    @Autowired
    private UmsAddressMapper addressMapper;

    @Override
    public List<UmsAddress> list(String keyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        UmsAddressExample example = new UmsAddressExample();
        example.createCriteria().andNameEqualTo("%" + keyword + "%");
        List<UmsAddress> addressList = addressMapper.selectByExample(example);
        return addressList;
    }
}
