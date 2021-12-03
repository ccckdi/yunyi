package com.cy.yunyi.admin.service;


import com.cy.yunyi.admin.dto.UmsMenuNode;
import com.cy.yunyi.model.UmsMenu;

import java.util.List;

/**
 * @Author: chx
 * @Description: 后台菜单管理Service
 * @DateTime: 2021/11/25 10:30
 **/
public interface UmsMenuService {
    /**
     * 创建后台菜单
     */
    int create(UmsMenu umsMenu);

    /**
     * 修改后台菜单
     */
    int update(Long id, UmsMenu umsMenu);

    /**
     * 根据ID获取菜单详情
     */
    UmsMenu getItem(Long id);

    /**
     * 根据ID删除菜单
     */
    int delete(Long id);

    /**
     * 分页查询后台菜单
     */
    List<UmsMenu> list(Long parentId, Integer pageSize, Integer pageNum);

    /**
     * 树形结构返回所有菜单列表
     */
    List<UmsMenuNode> treeList();

    /**
     * 修改菜单显示状态
     */
    int updateHidden(Long id, Integer hidden);
}
