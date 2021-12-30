package com.cy.portal.service.impl;

import com.cy.portal.service.AddressService;
import com.cy.yunyi.common.api.CommonResult;
import com.cy.yunyi.mapper.UmsAddressMapper;
import com.cy.yunyi.model.UmsAddress;
import com.cy.yunyi.model.UmsAddressExample;
import com.cy.yunyi.model.UmsAdminExample;
import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author: chx
 * @Description: TODO
 * @DateTime: 2021/12/28 22:56
 **/
@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private UmsAddressMapper addressMapper;

    @Override
    public List<UmsAddress> list(Long userId) {
        UmsAddressExample example = new UmsAddressExample();
        UmsAddressExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andStatusEqualTo(1);
        List<UmsAddress> addressList = addressMapper.selectByExample(example);
        return addressList;
    }

    @Override
    public Integer resetDefault(Long userId) {
        UmsAddressExample example = new UmsAddressExample();
        UmsAddressExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andIsDefaultEqualTo(1);
        criteria.andStatusEqualTo(1);
        List<UmsAddress> addressList = addressMapper.selectByExample(example);
        if (addressList.size() > 0) {
            UmsAddress address = addressList.get(0);
            address.setIsDefault(0);
            address.setUpdateTime(new Date());
            int count = addressMapper.updateByPrimaryKeySelective(address);
            return count;
        }
        return null;
    }

    @Override
    public UmsAddress getAddressById(Long id) {
        return addressMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer addAddress(Long userId, UmsAddress address) {
        if (address.getIsDefault() == 1) {
            // 重置其他收货地址的默认选项
            this.resetDefault(userId);
        }
        address.setUserId(userId);
        address.setCreateTime(new Date());
        address.setStatus(1);
        int count = addressMapper.insert(address);
        return count;
    }

    @Override
    public Integer updateAddress(Long userId, UmsAddress address) {
        if (address.getIsDefault() == 1) {
            // 重置其他收货地址的默认选项
            this.resetDefault(userId);
        }
        address.setUserId(userId);
        address.setUpdateTime(new Date());
        int count = addressMapper.updateByPrimaryKeySelective(address);
        return count;
    }

    @Override
    public UmsAddress findDefault(Long userId) {
        UmsAddressExample example = new UmsAddressExample();
        UmsAddressExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andIsDefaultEqualTo(1);
        criteria.andStatusEqualTo(1);
        List<UmsAddress> addressList = addressMapper.selectByExample(example);
        if (addressList.size() > 0){
            return addressList.get(0);
        }
        return null;
    }
}
