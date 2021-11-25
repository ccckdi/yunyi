package com.cy.yunyi.admin.component;

import com.cy.yunyi.admin.service.UmsResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author: chx
 * @Description: 资源与角色访问对应关系操作组件
 * @DateTime: 2021/11/24 16:50
 **/
@Component
public class ResourceRoleRulesHolder {

    @Autowired
    private UmsResourceService resourceService;

    @PostConstruct
    public void initResourceRolesMap(){
        resourceService.initResourceRolesMap();
    }
}

