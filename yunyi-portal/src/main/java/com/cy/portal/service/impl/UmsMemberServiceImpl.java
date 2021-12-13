package com.cy.portal.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.json.JSONUtil;
import com.cy.portal.dto.UserInfo;
import com.cy.portal.service.AuthService;
import com.cy.portal.service.UmsMemberCacheService;
import com.cy.portal.service.UmsMemberService;
import com.cy.yunyi.common.api.CommonResult;
import com.cy.yunyi.common.api.ResultCode;
import com.cy.yunyi.common.constant.AuthConstant;
import com.cy.yunyi.common.domain.UserDto;
import com.cy.yunyi.common.exception.Asserts;
import com.cy.yunyi.common.util.IpUtil;
import com.cy.yunyi.mapper.UmsMemberMapper;
import com.cy.yunyi.model.UmsMember;
import com.cy.yunyi.model.UmsMemberExample;
import org.apache.tomcat.util.http.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;


/**
 * @Author: chx
 * @Description: 会员管理Service实现类
 * @DateTime: 2021/12/9 11:20
 **/
@Service
public class UmsMemberServiceImpl implements UmsMemberService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UmsMemberServiceImpl.class);
    @Autowired
    private UmsMemberMapper memberMapper;
    @Autowired
    private WxMaService wxService;
    @Autowired
    private AuthService authService;
    @Autowired
    private UmsMemberCacheService memberCacheService;
    @Autowired
    private HttpServletRequest request;

    @Override
    public UmsMember getByUsername(String username) {
        UmsMemberExample example = new UmsMemberExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<UmsMember> memberList = memberMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(memberList)) {
            return memberList.get(0);
        }
        return null;
    }

    @Override
    public UmsMember getById(Long id) {
        return memberMapper.selectByPrimaryKey(id);
    }

    @Override
    public UmsMember getByOpenId(String openId) {
        UmsMemberExample example = new UmsMemberExample();
        example.or().andWeixinOpenidEqualTo(openId).andStatusEqualTo(1);
        List<UmsMember> umsMembers = memberMapper.selectByExample(example);
        if (umsMembers.size() > 0){
            return umsMembers.get(0);
        }
        return null;
    }

    @Override
    public CommonResult register(String username, String password, String telephone, String authCode) {
        //验证验证码
        if(!verifyAuthCode(authCode,telephone)){
            Asserts.fail("验证码错误");
        }
        //查询是否已有该用户
        UmsMemberExample example = new UmsMemberExample();
        example.createCriteria().andUsernameEqualTo(username);
        example.or(example.createCriteria().andTelephoneEqualTo(telephone));
        List<UmsMember> umsMembers = memberMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(umsMembers)) {
            Asserts.fail("该用户已经存在");
            return CommonResult.failed("该用户已经存在！");
        }
        //没有该用户进行添加操作
        UmsMember umsMember = new UmsMember();
        umsMember.setUsername(username);
        //昵称默认和用户名相同
        umsMember.setNickname(username);
        umsMember.setTelephone(telephone);
        umsMember.setPassword(BCrypt.hashpw(password));
        umsMember.setUserLevel(0);
        umsMember.setCreateTime(new Date());
        umsMember.setGender(0);
        umsMember.setLastLoginTime(new Date());
        umsMember.setLastLoginIp(IpUtil.getIpAddr(request));
        umsMember.setStatus(1);
        memberMapper.insert(umsMember);
        return CommonResult.success("注册成功！");
    }

    @Override
    public CommonResult register(String username, String password, String telephone, String authCode, String wxCode) {
        //验证验证码
        if(!verifyAuthCode(authCode,telephone)){
            //Asserts.fail("验证码错误");
        }
        //查询是否已有该用户
        UmsMemberExample example = new UmsMemberExample();
        example.createCriteria().andUsernameEqualTo(username);
        example.or(example.createCriteria().andTelephoneEqualTo(telephone));
        List<UmsMember> umsMembers = memberMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(umsMembers)) {
            Asserts.fail("该用户已经存在");
            return CommonResult.failed("该用户已经存在！");
        }
        String openId = "";
        try {
            WxMaJscode2SessionResult result = this.wxService.getUserService().getSessionInfo(wxCode);
            openId = result.getOpenid();
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(ResultCode.AUTH_OPENID_UNACCESS, "openid 获取失败");
        }
        List<UmsMember> memberList = queryByOpenid(openId);
        if (memberList.size() > 1) {
            return CommonResult.failed();
        }
        if (memberList.size() == 1) {
            return CommonResult.failed(ResultCode.AUTH_OPENID_BINDED, "openid已绑定账号");
        }

        UmsMember umsMember = null;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(password);

        umsMember = new UmsMember();
        umsMember.setUsername(username);
        umsMember.setPassword(encodedPassword);
        umsMember.setTelephone(telephone);
        umsMember.setWeixinOpenid(openId);
        umsMember.setIcon("https://yanxuan.nosdn.127.net/80841d741d7fa3073e0ae27bf487339f.jpg?imageView&quality=90&thumbnail=64x64");
        umsMember.setNickname(username);
        umsMember.setGender(0);
        umsMember.setUserLevel(0);
        umsMember.setStatus(1);
        umsMember.setLastLoginTime(new Date());
        umsMember.setLastLoginIp(IpUtil.getIpAddr(request));
        int count = memberMapper.insert(umsMember);

        if (count == 0){
            return null;
        }

        // userInfo
        UserInfo userInfo = new UserInfo();
        userInfo.setNickName(username);
        userInfo.setAvatarUrl(umsMember.getIcon());

         return CommonResult.success(userInfo);
    }

    @Override
    public String generateAuthCode(String telephone) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for(int i=0;i<6;i++){
            sb.append(random.nextInt(10));
        }
        memberCacheService.setAuthCode(telephone,sb.toString());
        return sb.toString();
    }

    @Override
    public CommonResult updatePassword(String telephone, String password, String authCode) {
        UmsMemberExample example = new UmsMemberExample();
        example.createCriteria().andTelephoneEqualTo(telephone);
        List<UmsMember> memberList = memberMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(memberList)){
            Asserts.fail("该账号不存在！");
            return CommonResult.failed("该账号不存在！");
        }
        //验证验证码
        if(!verifyAuthCode(authCode,telephone)){
            Asserts.fail("验证码错误！");
            return CommonResult.failed("验证码错误！");
        }
        UmsMember umsMember = memberList.get(0);
        umsMember.setPassword(BCrypt.hashpw(password));
        umsMember.setUpdateTime(new Date());
        memberMapper.updateByPrimaryKeySelective(umsMember);
        memberCacheService.delMember(umsMember.getId());
        return CommonResult.success("修改成功！");
    }

    @Override
    public UmsMember getCurrentMember() {
        String memberStr = request.getHeader(AuthConstant.USER_TOKEN_HEADER);
        if(StrUtil.isEmpty(memberStr)){
            Asserts.fail(ResultCode.UNAUTHORIZED);
        }
        UserDto userDto = JSONUtil.toBean(memberStr, UserDto.class);
        UmsMember member = memberCacheService.getMember(userDto.getId());
        if(member!=null){
            return member;
        }else{
            member = getById(userDto.getId());
            memberCacheService.setMember(member);
            return member;
        }
    }

    @Override
    public List<UmsMember> queryByOpenid(String openid) {
        UmsMemberExample umsMemberExample = new UmsMemberExample();
        umsMemberExample.createCriteria().andWeixinOpenidEqualTo(openid);
        List<UmsMember> umsMembers = memberMapper.selectByExample(umsMemberExample);
        return umsMembers;
    }

    @Override
    public UserDto loadUserByUsername(String username) {
        UmsMember member = getByUsername(username);
        if(member!=null){
            UserDto userDto = new UserDto();
            BeanUtil.copyProperties(member,userDto);
            userDto.setRoles(CollUtil.toList("前台会员"));
            return userDto;
        }
        return null;
    }

    @Override
    public CommonResult login(String username, String password) {
        if(StrUtil.isEmpty(username)||StrUtil.isEmpty(password)){
            Asserts.fail("用户名或密码不能为空！");
        }
        Map<String, String> params = new HashMap<>();
        params.put("client_id", AuthConstant.PORTAL_CLIENT_ID);
        params.put("client_secret","123456");
        params.put("grant_type","password");
        params.put("username",username);
        params.put("password",password);

        CommonResult accessToken = authService.getAccessToken(params);

        if (accessToken.getCode() != ResultCode.SUCCESS.getCode()){
            return accessToken;
        }

        Map resultMap = (Map) accessToken.getData();
        String token = (String) resultMap.get("token");

        UmsMember umsMember = getByUsername(username);

        // userInfo
        UserInfo userInfo = new UserInfo();
        userInfo.setNickName(username);
        userInfo.setAvatarUrl(umsMember.getIcon());

        Map<Object, Object> result = new HashMap<Object, Object>();
        result.put("token", token);
        result.put("userInfo", userInfo);
        return CommonResult.success(result);
    }

    @Override
    public CommonResult loginByWeixin(HttpServletRequest request, String code, UserInfo userInfo) {
        String sessionKey = null;
        String openId = null;
        try {
            WxMaJscode2SessionResult result = this.wxService.getUserService().getSessionInfo(code);
            sessionKey = result.getSessionKey();
            openId = result.getOpenid();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (sessionKey == null || openId == null) {
            return CommonResult.failed();
        }

        // token
        String token = "";

        UmsMember member = getByOpenId(openId);
        if (member == null) {
            member = new UmsMember();
            member.setUsername(openId);
            member.setPassword(openId);
            member.setWeixinOpenid(openId);
            member.setIcon(userInfo.getAvatarUrl());
            member.setNickname(userInfo.getNickName());
            member.setGender(userInfo.getGender());
            member.setUserLevel(0);
            member.setStatus(1);
            member.setLastLoginTime(new Date());
            member.setLastLoginIp(IpUtil.getIpAddr(request));
            member.setSessionKey(sessionKey);

            memberMapper.insert(member);

        } else {
            member.setLastLoginTime(new Date());
            member.setLastLoginIp(IpUtil.getIpAddr(request));
            member.setSessionKey(sessionKey);
            if (memberMapper.updateByPrimaryKeySelective(member) == 0) {
                return CommonResult.failed();
            }

            Map<String, String> params = new HashMap<>();
            params.put("client_id", AuthConstant.PORTAL_CLIENT_ID);
            params.put("loadByWeixin", "true");
            params.put("client_secret","123456");
            params.put("grant_type","password");
            params.put("username",member.getUsername());
            params.put("password",member.getPassword());
            CommonResult accessToken = authService.getAccessToken(params);

            if (accessToken.getCode() != ResultCode.SUCCESS.getCode()){
                return accessToken;
            }

            Map resultMap = (Map) accessToken.getData();

            token = (String) resultMap.get("token");
        }


        Map<Object, Object> result = new HashMap<Object, Object>();
        result.put("token", token);
        result.put("userInfo", userInfo);
        return CommonResult.success(result);
    }

    //对输入的验证码进行校验
    private boolean verifyAuthCode(String authCode, String telephone){
        if(StringUtils.isEmpty(authCode)){
            return false;
        }
        String realAuthCode = memberCacheService.getAuthCode(telephone);
        return authCode.equals(realAuthCode);
    }
}
