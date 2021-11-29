package com.cy.yunyi.admin.service.impl;

import com.cy.yunyi.admin.service.UmsAdminCacheService;
import com.cy.yunyi.common.service.RedisService;
import com.cy.yunyi.model.UmsAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Author: chx
 * @Description: 后台用户缓存操作类
 * @DateTime: 2021/11/29 9:29
 **/
@Service
public class UmsAdminCacheServiceImpl implements UmsAdminCacheService {

    @Autowired
    private RedisService redisService;
    @Value("${redis.database}")
    private String REDIS_DATABASE;
    @Value("${redis.expire.common}")
    private Long REDIS_EXPIRE;
    @Value("${redis.key.admin}")
    private String REDIS_KEY_ADMIN;

    @Override
    public void delAdmin(Long adminId) {
        String key =REDIS_DATABASE+":"+REDIS_KEY_ADMIN+":"+adminId;
        redisService.del(key);
    }

    @Override
    public UmsAdmin getAdmin(Long adminId) {
        String key =REDIS_DATABASE+":"+REDIS_KEY_ADMIN+":"+adminId;
        return (UmsAdmin) redisService.get(key);
    }

    @Override
    public void setAdmin(UmsAdmin admin) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + admin.getId();
        redisService.set(key, admin, REDIS_EXPIRE);
    }
}
