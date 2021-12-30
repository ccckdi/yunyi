package com.cy.portal.service;

import com.cy.portal.vo.RegionVo;
import com.cy.yunyi.model.OmsCart;

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
