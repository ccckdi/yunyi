package com.cy.portal.service;


import com.cy.yunyi.model.UmsMember;

/**
 * @Author: chx
 * @Description: 会员信息缓存业务类
 * @DateTime: 2021/12/9 11:20
 **/
public interface UmsMemberCacheService {
    /**
     * 删除会员用户缓存
     */
    void delMember(Long memberId);

    /**
     * 获取会员用户缓存
     */
    UmsMember getMember(Long memberId);

    /**
     * 设置会员用户缓存
     */
    void setMember(UmsMember member);

    /**
     * 设置验证码
     */
    void setAuthCode(String telephone, String authCode);

    /**
     * 获取验证码
     */
    String getAuthCode(String telephone);
}
