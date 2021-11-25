package com.cy.yunyi.auth.service.impl;

import com.cy.yunyi.common.constant.AuthConstant;
import com.cy.yunyi.common.service.RedisService;
import com.cy.yunyi.mapper.UmsResourceMapper;
import com.cy.yunyi.mapper.UmsRoleMapper;
import com.cy.yunyi.mapper.UmsRoleResourceRelationMapper;
import com.cy.yunyi.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @Author: chx
 * @Description: 资源与角色匹配关系管理业务类
 * @DateTime: 2021/11/16 11:27

@Service
public class ResourceServiceImpl {


    @Autowired
    private UmsResourceMapper resourceMapper;

    @Autowired
    private UmsRoleMapper roleMapper;

    @Autowired
    private UmsRoleResourceRelationMapper roleResourceRelationMapper;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private RedisService redisService;

    //加载资源路径-角色到redis
    @PostConstruct
    public void initData() {
        Map<String, List<String>> resourceRolesMap = new TreeMap<>();
        List<UmsResource> resourceList = resourceMapper.selectByExample(new UmsResourceExample());
        List<UmsRole> roleList = roleMapper.selectByExample(new UmsRoleExample());
        List<UmsRoleResourceRelation> relationList = roleResourceRelationMapper.selectByExample(new UmsRoleResourceRelationExample());
        for (UmsResource resource : resourceList) {
            Set<Long> roleIds = relationList.stream().filter(item -> item.getResourceId().equals(resource.getId())).map(UmsRoleResourceRelation::getRoleId).collect(Collectors.toSet());
            List<String> roleNames = roleList.stream().filter(item -> roleIds.contains(item.getId())).map(item -> item.getId() + "_" + item.getName()).collect(Collectors.toList());
            resourceRolesMap.put(resource.getUrl(),roleNames);
        }
        redisService.del(AuthConstant.RESOURCE_ROLES_MAP_KEY);
        redisService.hSetAll(AuthConstant.RESOURCE_ROLES_MAP_KEY, resourceRolesMap);
    }
}
 **/