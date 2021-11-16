package com.cy.yunyi.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cy.yunyi.auth.constant.MessageConstant;
import com.cy.yunyi.auth.domain.SecurityUser;
import com.cy.yunyi.auth.domain.Users;
import com.cy.yunyi.auth.mapper.UsersMapper;
import com.cy.yunyi.common.domain.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: chx
 * @Description: TODO
 * @DateTime: 2021/11/9 11:05
 **/
@Service
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private UsersMapper usersMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        QueryWrapper<Users> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        Users users = usersMapper.selectOne(wrapper);

        if (users == null){
            throw new UsernameNotFoundException(MessageConstant.USERNAME_PASSWORD_ERROR);
        }

        UserDto userDto = new UserDto();
        SecurityUser securityUser = new SecurityUser(userDto);
        return securityUser;
    }
}
