package com.cy.yunyi.admin.service;

import com.cy.yunyi.model.UmsAdmin;

/**
 * @Author: chx
 * @Description: 后台用户缓存操作类
 * @DateTime: 2021/11/29 9:28
 **/
public interface UmsAdminCacheService {
    /**
     * 删除后台用户缓存
     */
    void delAdmin(Long adminId);

    /**
     * 获取缓存后台用户信息
     */
    UmsAdmin getAdmin(Long adminId);

    /**
     * 设置缓存后台用户信息
     */
    void setAdmin(UmsAdmin admin);
}
