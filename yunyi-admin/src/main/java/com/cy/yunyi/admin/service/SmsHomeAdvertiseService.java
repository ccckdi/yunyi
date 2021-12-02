package com.cy.yunyi.admin.service;

import com.cy.yunyi.model.SmsHomeAdvertise;

import java.util.List;

/**
 * @Author: chx
 * @Description: 广告管理Service
 * @DateTime: 2021/11/30 11:00
 **/
public interface SmsHomeAdvertiseService {
    int create(SmsHomeAdvertise advertise);

    int update(Long id, SmsHomeAdvertise advertise);

    List<SmsHomeAdvertise> list(String keyword, Integer pageSize, Integer pageNum);

    int delete(Long id);
}
