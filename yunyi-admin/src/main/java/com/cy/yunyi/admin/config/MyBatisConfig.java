package com.cy.yunyi.admin.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author: chx
 * @Description: MyBatis相关配置
 * @DateTime: 2021/11/24 10:07
 **/
@Configuration
@EnableTransactionManagement
@MapperScan({"com.cy.yunyi.mapper","com.cy.yunyi.admin.dao"})
public class MyBatisConfig {

}
