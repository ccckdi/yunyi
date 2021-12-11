package com.cy.portal.service;

import com.cy.yunyi.common.api.CommonResult;
import com.cy.yunyi.common.domain.UserDto;

import com.cy.yunyi.model.UmsMember;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: chx
 * @Description: 会员管理Service
 * @DateTime: 2021/12/9 11:20
 **/
public interface UmsMemberService {
    /**
     * 根据用户名获取会员
     */
    UmsMember getByUsername(String username);

    /**
     * 根据会员编号获取会员
     */
    UmsMember getById(Long id);

    /**
     * 用户注册
     */
    @Transactional
    CommonResult register(String username, String password, String telephone, String authCode);

    /**
     * 生成验证码
     */
    String generateAuthCode(String telephone);

    /**
     * 修改密码
     */
    @Transactional
    CommonResult updatePassword(String telephone, String password, String authCode);

    /**
     * 获取当前登录会员
     */
    UmsMember getCurrentMember();

    /**
     * 获取用户信息
     */
    UserDto loadUserByUsername(String username);

    /**
     * 登录后获取token
     */
    CommonResult login(String username, String password);
}
