package com.cy.portal.service;

import com.cy.portal.vo.HomeContentVo;
import com.cy.portal.vo.UserContentVo;

/**
 * @Author: chx
 * @Description: 个人中心Service
 * @DateTime: 2022/1/26 11:43
 **/
public interface UserService {

    /**
     * 获取个人中心内容
     */
    UserContentVo content(Long userId);
}
