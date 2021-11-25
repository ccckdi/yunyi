package com.cy.yunyi.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.cy.yunyi.admin.dao.UmsAdminRoleRelationDao;
import com.cy.yunyi.admin.service.AdminService;
import com.cy.yunyi.common.domain.UserDto;
import com.cy.yunyi.mapper.UmsAdminMapper;
import com.cy.yunyi.model.UmsAdmin;
import com.cy.yunyi.model.UmsAdminExample;
import com.cy.yunyi.model.UmsRole;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: chx
 * @Description: 后台管理员Service
 * @DateTime: 2021/11/24 9:55
 **/
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UmsAdminMapper adminMapper;

    @Autowired
    private UmsAdminRoleRelationDao adminRoleRelationDao;

    @Override
    public UmsAdmin getAdminByUsername(String username) {
        UmsAdminExample example = new UmsAdminExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<UmsAdmin> adminList = adminMapper.selectByExample(example);
        if (adminList != null && adminList.size() > 0) {
            return adminList.get(0);
        }
        return null;
    }

    @Override
    public List<UmsRole> getRoleList(Long adminId) {
        return adminRoleRelationDao.getRoleList(adminId);
    }

    @Override
    public UserDto loadUserByUsername(String username) {
        //获取用户信息
        UmsAdmin admin = getAdminByUsername(username);
        if (admin == null) return null;
        List<UmsRole> roleList = getRoleList(admin.getId());
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(admin,userDto);
        if(CollUtil.isNotEmpty(roleList)){
            List<String> roleStrList = roleList.stream().map(item -> item.getId() + "_" + item.getName()).collect(Collectors.toList());
            userDto.setRoles(roleStrList);
        }
        return userDto;
    }
}
