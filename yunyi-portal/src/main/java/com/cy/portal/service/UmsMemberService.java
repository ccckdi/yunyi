package com.cy.portal.service;

import com.cy.portal.dto.UserInfo;
import com.cy.yunyi.common.api.CommonResult;
import com.cy.yunyi.common.domain.UserDto;

import com.cy.yunyi.model.UmsMember;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    UmsMember getByOpenId(String openId);

    /**
     * 会员注册
     */
    @Transactional
    CommonResult register(String username, String password, String telephone, String authCode);

    /**
     * 会员注册(wx)
     */
    @Transactional
    CommonResult register(String username, String password, String telephone, String authCode,String wxCode);

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
     * 根据openid获取会员
     */
    List<UmsMember> queryByOpenid(String openid);

    /**
     * 获取用户信息
     */
    UserDto loadUserByUsername(String username);

    /**
     * 登录后获取token
     */
    CommonResult login(String username, String password);

    /**
     *微信登录
     */
    CommonResult loginByWeixin(HttpServletRequest request, String code, UserInfo userInfo);
}
