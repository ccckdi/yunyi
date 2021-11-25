package com.cy.yunyi.admin.service;

import com.cy.yunyi.common.domain.UserDto;
import com.cy.yunyi.model.UmsAdmin;
import com.cy.yunyi.model.UmsRole;

import java.util.List;

/**
 * @Author: chx
 * @Description: 后台管理员Service
 * @DateTime: 2021/11/24 9:53
 **/
public interface AdminService {

    UmsAdmin getAdminByUsername(String username);

    List<UmsRole> getRoleList(Long adminId);

    UserDto loadUserByUsername(String username);
}
