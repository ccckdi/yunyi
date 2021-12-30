package com.cy.portal.service;

import com.cy.yunyi.model.UmsAddress;

import java.util.List;

/**
 * @Author: chx
 * @Description: 收货地址Service
 * @DateTime: 2021/12/28 22:48
 **/
public interface AddressService {

    List<UmsAddress> list(Long userId);

    Integer resetDefault(Long userId);

    UmsAddress getAddressById(Long id);

    Integer addAddress(Long userId,UmsAddress address);

    Integer updateAddress(Long userId,UmsAddress address);

    UmsAddress findDefault(Long userId);
}
