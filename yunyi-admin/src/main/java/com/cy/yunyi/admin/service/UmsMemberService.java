package com.cy.yunyi.admin.service;

import com.cy.yunyi.admin.dto.UpdateUserPasswordParam;
import com.cy.yunyi.model.UmsMember;

import java.util.List;

/**
 * @Author: chx
 * @Description: 用户管理Service
 * @DateTime: 2021/11/25 14:43
 **/
public interface UmsMemberService {

    List<UmsMember> list(String keyword, Integer pageSize, Integer pageNum);

    UmsMember getUser(Long id);

    int update(Long id, UmsMember user);

    int updatePassword(UpdateUserPasswordParam updateUserPasswordParam);

    int delete(Long id);
}
