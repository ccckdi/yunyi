package com.cy.yunyi.admin.service;

import com.cy.yunyi.admin.dto.UpdateUserPasswordParam;
import com.cy.yunyi.model.UmsUser;

import java.util.List;

/**
 * @Author: chx
 * @Description: 用户管理Service
 * @DateTime: 2021/11/25 14:43
 **/
public interface UmsUserService {

    List<UmsUser> list(String keyword, Integer pageSize, Integer pageNum);

    UmsUser getUser(Long id);

    int update(Long id, UmsUser user);

    int updatePassword(UpdateUserPasswordParam updateUserPasswordParam);

    int delete(Long id);
}
