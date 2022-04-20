package com.cy.yunyi.portal.config;

import com.cy.yunyi.common.config.BaseRedisConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: chx
 * @Description: Redis相关配置
 * @DateTime: 2021/12/9 11:20
 **/
@EnableCaching
@Configuration
public class RedisConfig extends BaseRedisConfig {

}