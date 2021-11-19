package com.cy.yunyi.auth.service;

import com.cy.yunyi.common.domain.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: chx
 * @Description: 管理员service
 * @DateTime: 2021/11/19 22:43
 **/
@FeignClient("yunyi-admin")
public interface UmsAdminService {
    @GetMapping("/admin/loadByUsername")
    UserDto loadUserByUsername(@RequestParam String username);
}
