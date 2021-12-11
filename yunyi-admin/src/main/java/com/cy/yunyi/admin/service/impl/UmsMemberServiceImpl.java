package com.cy.yunyi.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.cy.yunyi.admin.dto.UpdateUserPasswordParam;
import com.cy.yunyi.admin.service.UmsMemberService;
import com.cy.yunyi.mapper.UmsMemberMapper;
import com.cy.yunyi.model.UmsMember;
import com.cy.yunyi.model.UmsMemberExample;
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
public class UmsMemberServiceImpl implements UmsMemberService {

    @Autowired
    private UmsMemberMapper userMapper;

    @Override
    public List<UmsMember> list(String keyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        UmsMemberExample example = new UmsMemberExample();
        if (!org.springframework.util.StringUtils.isEmpty(keyword)) {
            example.createCriteria().andUsernameLike("%" + keyword + "%");
        }
        List<UmsMember> userList = userMapper.selectByExample(example);
        return userList;
    }

    @Override
    public UmsMember getUser(Long id) {
        UmsMember user = userMapper.selectByPrimaryKey(id);
        return user;
    }

    @Override
    public int update(Long id, UmsMember user) {
        user.setId(id);
        UmsMember rawUser = userMapper.selectByPrimaryKey(id);
        if (rawUser.getPassword().equals(user.getPassword())){
            user.setPassword(null);
        }else if (StringUtils.isEmpty(user.getPassword())){
            user.setPassword(null);
        }else {
            user.setPassword(BCrypt.hashpw(user.getPassword()));
        }
        int count = userMapper.updateByPrimaryKeySelective(user);
        return count;
    }

    @Override
    public int updatePassword(UpdateUserPasswordParam param) {
        if(StrUtil.isEmpty(param.getUsername())
                ||StrUtil.isEmpty(param.getOldPassword())
                || StrUtil.isEmpty(param.getNewPassword())){
            return -1;
        }
        UmsMemberExample example = new UmsMemberExample();
        example.createCriteria().andUsernameEqualTo(param.getUsername());
        List<UmsMember> userList = userMapper.selectByExample(example);
        if(CollUtil.isEmpty(userList)){
            return -2;
        }
        UmsMember umsUser = userList.get(0);
        if(!BCrypt.checkpw(param.getOldPassword(),umsUser.getPassword())){
            return -3;
        }
        umsUser.setPassword(BCrypt.hashpw(param.getNewPassword()));
        userMapper.updateByPrimaryKeySelective(umsUser);
        return 1;
    }

    @Override
    public int delete(Long id) {
        int count = userMapper.deleteByPrimaryKey(id);
        return count;
    }
}
