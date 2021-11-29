package com.cy.yunyi.admin.service;

import com.cy.yunyi.admin.dto.UmsAdminParam;
import com.cy.yunyi.admin.dto.UpdateAdminPasswordParam;
import com.cy.yunyi.common.api.CommonResult;
import com.cy.yunyi.common.domain.UserDto;
import com.cy.yunyi.model.UmsAdmin;
import com.cy.yunyi.model.UmsRole;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: chx
 * @Description: 后台管理员Service
 * @DateTime: 2021/11/24 9:53
 **/
public interface UmsAdminService {

    /**
     * 根据用户名获取后台管理员
     */
    UmsAdmin getAdminByUsername(String username);

    /**
     * 获取用户信息
     */
    UserDto loadUserByUsername(String username);

    /**
     * 注册
     */
    UmsAdmin register(UmsAdminParam umsAdminParam);

    /**
     * 登录功能
     * @param username 用户名
     * @param password 密码
     * @return 调用认证中心返回结果
     */
    CommonResult login(String username, String password);

    /**
     * 根据用户id获取用户
     */
    UmsAdmin getAdmin(Long id);

    /**
     * 根据用户名或昵称分页查询用户
     */
    List<UmsAdmin> list(String keyword, Integer pageSize, Integer pageNum);

    /**
     * 修改指定用户信息
     */
    int update(Long id, UmsAdmin admin);

    /**
     * 删除指定用户
     */
    int delete(Long id);

    /**
     * 修改用户角色关系
     */
    @Transactional
    int updateRole(Long adminId, List<Long> roleIds);

    /**
     * 根据用户id获取用户角色
     */
    List<UmsRole> getRoleList(Long adminId);

    /**
     * 获取当前登录后台用户
     */
    UmsAdmin getCurrentAdmin();

    /**
     * 修改密码
     */
    int updatePassword(UpdateAdminPasswordParam updatePasswordParam);
}
