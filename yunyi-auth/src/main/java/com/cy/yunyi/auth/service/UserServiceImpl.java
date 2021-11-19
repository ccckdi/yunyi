package com.cy.yunyi.auth.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cy.yunyi.auth.constant.MessageConstant;
import com.cy.yunyi.auth.domain.SecurityUser;
import com.cy.yunyi.auth.domain.Users;
import com.cy.yunyi.auth.mapper.UsersMapper;
import com.cy.yunyi.common.domain.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @Author: chx
 * @Description: TODO
 * @DateTime: 2021/11/9 11:05
 **/
@Service
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

//        QueryWrapper<Users> wrapper = new QueryWrapper<>();
//        wrapper.eq("username",username);
//        Users users = usersMapper.selectOne(wrapper);

        if (username == "test"){
            throw new UsernameNotFoundException(MessageConstant.USERNAME_PASSWORD_ERROR);
        }

        String password = passwordEncoder.encode("123456");
        UserDto userDto = new UserDto(1L,username, password,1, "client-app",CollUtil.toList("ADMIN"));
        SecurityUser securityUser = new SecurityUser(userDto);
        return securityUser;
    }
}
