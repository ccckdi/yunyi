package com.cy.yunyi.portal.service;

import com.cy.yunyi.common.api.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @Author: chx
 * @Description: TODO
 * @DateTime: 2021/11/25 15:27
 **/
@FeignClient("yunyi-auth")
public interface AuthService {

    @PostMapping(value = "/oauth/token")
    CommonResult getAccessToken(@RequestParam Map<String, String> parameters);
}
