package com.cy.yunyi.admin.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: chx
 * @Description: 商品分类vo
 * @DateTime: 2021/12/5 16:31
 **/
@Data
public class PmsCategoryVo {
    private Long id;
    private String name;
    private String keywords;
    private String detail;
    private String iconUrl;
    private String picUrl;
    private String level;
    private List<PmsCategoryVo> children;
}
