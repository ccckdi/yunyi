package com.cy.yunyi.portal.service.impl;

import com.cy.yunyi.portal.dto.ItemPreferencesDto;
import com.cy.yunyi.portal.service.ItemBaseCacheService;
import com.cy.yunyi.common.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author caihx
 * @Description: 推荐服务缓存Service实现类
 * @Date 2022/2/14
 */
@Service
public class ItemBaseCacheServiceImpl implements ItemBaseCacheService {

    @Autowired
    private RedisService redisService;
    @Value("${redis.database}")
    private String REDIS_DATABASE;
    @Value("${redis.expire.common}")
    private Long REDIS_EXPIRE;
    @Value("${redis.key.score}")
    private String REDIS_KEY_SCORE;

    @Override
    public void delScore(Long userId) {
        String key =REDIS_DATABASE+":"+REDIS_KEY_SCORE+":"+userId;
        redisService.del(key);
    }

    @Override
    public List<ItemPreferencesDto> getScore(Long userId) {
        String key =REDIS_DATABASE+":"+REDIS_KEY_SCORE+":"+userId;
        return (List) redisService.lRange(key,0,-1);
    }

    @Override
    public void putScore(ItemPreferencesDto ItemPreferencesDto) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_SCORE + ":" + ItemPreferencesDto.getUserId();
        redisService.lPush(key, ItemPreferencesDto, REDIS_EXPIRE);
    }
}
