package com.cy.yunyi.auth.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.cy.yunyi.common.constant.AuthConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Author: chx
 * @Description: 资源与角色匹配关系管理业务类
 * @DateTime: 2021/11/16 11:27
 **/
@Service
public class ResourceServiceImpl {

    private Map<String, List<String>> resourceRolesMap;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @PostConstruct
    public void initData() {
        resourceRolesMap = new TreeMap<>();
        resourceRolesMap.put("/api/hello", CollUtil.toList("ADMIN"));
        resourceRolesMap.put("/api/user/currentUser", CollUtil.toList("ADMIN", "TEST"));
        redisTemplate.opsForHash().putAll(AuthConstant.RESOURCE_ROLES_MAP_KEY, resourceRolesMap);
        redisTemplate.opsForHash().putAll(AuthConstant.RESOURCE_ROLES_MAP_KEY, resourceRolesMap);
    }
}
