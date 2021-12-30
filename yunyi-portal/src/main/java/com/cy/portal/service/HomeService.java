package com.cy.portal.service;

import com.cy.portal.vo.HomeContentVo;

/**
 * @Author: chx
 * @Description: 首页内容管理Service
 * @DateTime: 2021/12/13 23:33
 **/
public interface HomeService {

    /**
     * 获取首页内容
     */
    HomeContentVo content();
}
