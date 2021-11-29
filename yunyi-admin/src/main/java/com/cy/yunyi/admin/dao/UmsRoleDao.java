package com.cy.yunyi.admin.dao;

import com.cy.yunyi.model.UmsResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: chx
 * @Description: 台角色管理Dao
 * @DateTime: 2021/11/25 10:40
 **/
public interface UmsRoleDao {
//    /**
//     * 根据后台用户ID获取菜单
//     */
//    List<UmsMenu> getMenuList(@Param("adminId") Long adminId);
//    /**
//     * 根据角色ID获取菜单
//     */
//    List<UmsMenu> getMenuListByRoleId(@Param("roleId") Long roleId);
    /**
     * 根据角色ID获取资源
     */
    List<UmsResource> getResourceListByRoleId(@Param("roleId") Long roleId);
}
