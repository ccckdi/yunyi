package com.cy.yunyi.admin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * @Author: chx
 * @Description: 用户更新参数
 * @DateTime: 2021/11/25 14:51
 **/
@Data
public class UmsUserParam {
    @ApiModelProperty(value = "昵称")
    private String nickname;
    @ApiModelProperty(value = "性别")
    private String gender;
    @ApiModelProperty(value = "用户头像")
    private String icon;
}
