package com.cy.yunyi.portal.service;

import com.cy.yunyi.portal.vo.RegionVo;

import java.util.List;

/**
 * @Author: chx
 * @Description: 区域信息Service
 * @DateTime: 2021/12/28 20:59
 **/
public interface RegionService {

    /**
     * 查询区域信息
     */
    List<RegionVo> list();
}
