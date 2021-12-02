package com.cy.yunyi.admin.service;

import com.cy.yunyi.model.UmsAddress;

import java.util.List;

/**
 * @Author: chx
 * @Description: 收货地址Service
 * @DateTime: 2021/12/2 9:58
 **/
public interface UmsAddressService {
    List<UmsAddress> list(String keyword, Integer pageSize, Integer pageNum);

}
