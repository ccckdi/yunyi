package com.cy.yunyi.admin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * @Author: chx
 * @Description: 用户注册参数
 * @DateTime: 2021/11/25 14:51
 **/
@Data
public class UmsAdminParam {
    @ApiModelProperty(value = "用户名", required = true)
    @NotEmpty
    private String username;
    @ApiModelProperty(value = "密码", required = true)
    @NotEmpty
    private String password;
    @ApiModelProperty(value = "用户头像")
    private String icon;
    @ApiModelProperty(value = "邮箱")
    @Email
    private String email;
    @ApiModelProperty(value = "用户昵称")
    private String nickName;
    @ApiModelProperty(value = "备注")
    private String note;
}
