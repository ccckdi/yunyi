package com.cy.yunyi.portal.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author: chx
 * @Description: MyBatis相关配置
 * @DateTime: 2021/12/9 11:20
 **/
@Configuration
@EnableTransactionManagement
@MapperScan({"com.cy.yunyi.mapper","com.cy.yunyi.portal.dao"})
public class MyBatisConfig {

}
