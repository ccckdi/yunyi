package com.cy.yunyi.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.cy.yunyi.admin.dto.UpdateUserPasswordParam;
import com.cy.yunyi.admin.service.UmsUserService;
import com.cy.yunyi.mapper.UmsUserMapper;
import com.cy.yunyi.model.UmsUser;
import com.cy.yunyi.model.UmsUserExample;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: chx
 * @Description: 用户管理Service实现类
 * @DateTime: 2021/11/29 21:46
 **/
@Service
public class UmsUserServiceImpl implements UmsUserService {

    @Autowired
    private UmsUserMapper userMapper;

    @Override
    public List<UmsUser> list(String keyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        UmsUserExample example = new UmsUserExample();
        example.createCriteria().andUsernameLike("%" + keyword + "%");
        List<UmsUser> userList = userMapper.selectByExample(example);
        return userList;
    }

    @Override
    public UmsUser getUser(Long id) {
        UmsUser user = userMapper.selectByPrimaryKey(id);
        return user;
    }

    @Override
    public int update(Long id, UmsUser user) {
        user.setId(id);
        UmsUser rawUser = userMapper.selectByPrimaryKey(id);
        if (rawUser.getPassword().equals(user.getPassword())){
            user.setPassword(null);
        }else if (StringUtils.isEmpty(user.getPassword())){
            user.setPassword(null);
        }else {
            user.setPassword(BCrypt.hashpw(user.getPassword()));
        }
        int count = userMapper.updateByPrimaryKey(user);
        return count;
    }

    @Override
    public int updatePassword(UpdateUserPasswordParam param) {
        if(StrUtil.isEmpty(param.getUsername())
                ||StrUtil.isEmpty(param.getOldPassword())
                || StrUtil.isEmpty(param.getNewPassword())){
            return -1;
        }
        UmsUserExample example = new UmsUserExample();
        example.createCriteria().andUsernameEqualTo(param.getUsername());
        List<UmsUser> userList = userMapper.selectByExample(example);
        if(CollUtil.isEmpty(userList)){
            return -2;
        }
        UmsUser umsUser = userList.get(0);
        if(!BCrypt.checkpw(param.getOldPassword(),umsUser.getPassword())){
            return -3;
        }
        umsUser.setPassword(BCrypt.hashpw(param.getNewPassword()));
        userMapper.updateByPrimaryKey(umsUser);
        return 1;
    }

    @Override
    public int delete(Long id) {
        int count = userMapper.deleteByPrimaryKey(id);
        return count;
    }
}
