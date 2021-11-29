package com.cy.yunyi.admin.dao;

import com.cy.yunyi.model.UmsAdminRoleRelation;
import com.cy.yunyi.model.UmsResource;
import com.cy.yunyi.model.UmsRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: chx
 * @Description: 自定义后台用户与角色Dao
 * @DateTime: 2021/11/24 10:06
 **/
public interface UmsAdminRoleRelationDao {

    /**
     * 获取用于所有角色
     */
    List<UmsRole> getRoleList(@Param("adminId") Long adminId);

    /**
     * 获取用户所有可访问资源
     */
    List<UmsResource> getResourceList(@Param("adminId") Long adminId);

    /**
     * 获取资源相关用户ID列表
     */
    List<Long> getAdminIdList(@Param("resourceId") Long resourceId);

    /**
     * 批量插入用户角色关系
     */
    void insertList(List<UmsAdminRoleRelation> list);
}
