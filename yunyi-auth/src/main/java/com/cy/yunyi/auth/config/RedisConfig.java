package com.cy.yunyi.auth.config;

import com.cy.yunyi.common.config.BaseRedisConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: chx
 * @Description: Redis相关配置
 * @DateTime: 2021/11/24 9:30
 **/
@EnableCaching
@Configuration
public class RedisConfig extends BaseRedisConfig {

}