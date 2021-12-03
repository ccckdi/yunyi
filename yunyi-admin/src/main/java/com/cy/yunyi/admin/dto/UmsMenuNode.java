package com.cy.yunyi.admin.dto;

import com.cy.yunyi.model.UmsMenu;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Author: chx
 * @Description: 后台菜单节点封装
 * @DateTime: 2021/11/24 14:51
 **/
@Getter
@Setter
public class UmsMenuNode extends UmsMenu {
    @ApiModelProperty(value = "子级菜单")
    private List<UmsMenuNode> children;
}
