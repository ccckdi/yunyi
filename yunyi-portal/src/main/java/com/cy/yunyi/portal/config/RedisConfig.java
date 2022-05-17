package com.cy.yunyi.portal.config;

import com.cy.yunyi.common.config.BaseRedisConfig;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: chx
 * @Description: Redis相关配置
 * @DateTime: 2021/12/9 11:20
 **/
@EnableCaching
@Configuration
public class RedisConfig extends BaseRedisConfig {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private String port;

    @Bean
    public RedissonClient RedissonClient(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://"+host+":"+port);
        return Redisson.create(config);
    }
}