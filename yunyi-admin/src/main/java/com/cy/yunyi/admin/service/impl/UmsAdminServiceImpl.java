package com.cy.yunyi.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.json.JSONUtil;
import com.cy.yunyi.admin.dao.UmsAdminRoleRelationDao;
import com.cy.yunyi.admin.dto.UmsAdminParam;
import com.cy.yunyi.admin.dto.UpdateAdminPasswordParam;
import com.cy.yunyi.admin.service.UmsAdminService;
import com.cy.yunyi.admin.service.feign.AuthService;
import com.cy.yunyi.common.api.CommonResult;
import com.cy.yunyi.common.api.ResultCode;
import com.cy.yunyi.common.constant.AuthConstant;
import com.cy.yunyi.common.domain.UserDto;
import com.cy.yunyi.common.exception.Asserts;
import com.cy.yunyi.mapper.UmsAdminMapper;
import com.cy.yunyi.mapper.UmsAdminRoleRelationMapper;
import com.cy.yunyi.model.*;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: chx
 * @Description: 后台管理员Service
 * @DateTime: 2021/11/24 9:55
 **/
@Service
public class UmsAdminServiceImpl implements UmsAdminService {

    @Autowired
    private UmsAdminMapper adminMapper;

    @Autowired
    private UmsAdminRoleRelationMapper adminRoleRelationMapper;

    @Autowired
    private UmsAdminRoleRelationDao adminRoleRelationDao;

    @Autowired
    private AuthService authService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UmsAdminCacheServiceImpl  adminCacheService;

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

    @Override
    public UmsAdmin register(UmsAdminParam umsAdminParam) {
        UmsAdmin umsAdmin = new UmsAdmin();
        BeanUtils.copyProperties(umsAdminParam,umsAdmin);
        //判断是否已存在用户名
        UmsAdminExample example = new UmsAdminExample();
        example.createCriteria().andUsernameEqualTo(umsAdmin.getUsername());
        List<UmsAdmin> adminList = adminMapper.selectByExample(example);
        if (adminList.size() > 0){
            return null;
        }
        umsAdmin.setStatus(1);
        umsAdmin.setCreateTime(new Date());
        //对密码进行加密
        String encodePassword = BCrypt.hashpw(umsAdmin.getPassword());
        umsAdmin.setPassword(encodePassword);
        int count = adminMapper.insert(umsAdmin);
        if (count == 0){
            return null;
        }
        return umsAdmin;
    }

    @Override
    public CommonResult login(String username, String password) {
        if(StrUtil.isEmpty(username)||StrUtil.isEmpty(password)){
            Asserts.fail("用户名或密码不能为空！");
        }
        Map<String, String> params = new HashMap<>();
        params.put("client_id", AuthConstant.ADMIN_CLIENT_ID);
        params.put("client_secret","123456");
        params.put("grant_type","password");
        params.put("username",username);
        params.put("password",password);
        CommonResult restResult = authService.getAccessToken(params);
        if(ResultCode.SUCCESS.getCode()==restResult.getCode()&&restResult.getData()!=null){
            //updateLoginTimeByUsername(username);
            //insertLoginLog(username);
        }
        return restResult;
    }

    @Override
    public UmsAdmin getAdmin(Long id) {
        return adminMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<UmsAdmin> list(String keyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        UmsAdminExample example = new UmsAdminExample();
        UmsAdminExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameLike("%" + keyword + "%");
        example.or(example.createCriteria().andNickNameLike("%" + keyword + "%"));
        return adminMapper.selectByExample(example);
    }

    @Override
    public int update(Long id, UmsAdmin admin) {
        admin.setId(id);
        UmsAdmin rawAdmin = adminMapper.selectByPrimaryKey(id);
        if(rawAdmin.getPassword().equals(admin.getPassword())){
            //与原加密密码相同的不需要修改
            admin.setPassword(null);
        }else{
            //与原加密密码不同的需要加密修改
            if(StrUtil.isEmpty(admin.getPassword())){
                admin.setPassword(null);
            }else{
                admin.setPassword(BCrypt.hashpw(admin.getPassword()));
            }
        }
        int count = adminMapper.updateByPrimaryKeySelective(admin);
        adminCacheService.delAdmin(id);
        return count;
    }

    @Override
    public int delete(Long id) {
        int count = adminMapper.deleteByPrimaryKey(id);
        adminCacheService.delAdmin(id);
        return count;
    }

    @Override
    public int updateRole(Long adminId, List<Long> roleIds) {
        int count = roleIds == null ? 0 : roleIds.size();
        //删除原有关系
        UmsAdminRoleRelationExample example = new UmsAdminRoleRelationExample();
        example.createCriteria().andAdminIdEqualTo(adminId);
        adminRoleRelationMapper.deleteByExample(example);
        //建立新关系
        if (!CollectionUtils.isEmpty(roleIds)){
            List<UmsAdminRoleRelation> list = new ArrayList<>();
            for (Long roleId : roleIds) {
                UmsAdminRoleRelation roleRelation = new UmsAdminRoleRelation();
                roleRelation.setAdminId(adminId);
                roleRelation.setRoleId(roleId);
                list.add(roleRelation);
            }
            adminRoleRelationDao.insertList(list);
        }
        return count;
    }

    @Override
    public UmsAdmin getCurrentAdmin() {
        String userStr = request.getHeader(AuthConstant.USER_TOKEN_HEADER);
        if(StrUtil.isEmpty(userStr)){
            Asserts.fail(ResultCode.UNAUTHORIZED);
        }
        UserDto userDto = JSONUtil.toBean(userStr, UserDto.class);
        UmsAdmin admin = adminCacheService.getAdmin(userDto.getId());
        if(admin!=null){
            return admin;
        }else{
            admin = adminMapper.selectByPrimaryKey(userDto.getId());
            adminCacheService.setAdmin(admin);
            return admin;
        }
    }

    @Override
    public int updatePassword(UpdateAdminPasswordParam param) {
        if(StrUtil.isEmpty(param.getUsername())
                ||StrUtil.isEmpty(param.getOldPassword())
                ||StrUtil.isEmpty(param.getNewPassword())){
            return -1;
        }
        UmsAdminExample example = new UmsAdminExample();
        example.createCriteria().andUsernameEqualTo(param.getUsername());
        List<UmsAdmin> adminList = adminMapper.selectByExample(example);
        if(CollUtil.isEmpty(adminList)){
            return -2;
        }
        UmsAdmin umsAdmin = adminList.get(0);
        if(!BCrypt.checkpw(param.getOldPassword(),umsAdmin.getPassword())){
            return -3;
        }
        umsAdmin.setPassword(BCrypt.hashpw(param.getNewPassword()));
        adminMapper.updateByPrimaryKey(umsAdmin);
        adminCacheService.delAdmin(umsAdmin.getId());
        return 1;
    }


}
