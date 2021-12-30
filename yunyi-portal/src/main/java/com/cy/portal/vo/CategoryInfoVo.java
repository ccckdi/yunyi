package com.cy.portal.vo;

import com.cy.yunyi.model.PmsCategory;
import lombok.Data;

import java.util.List;

/**
 * @Author: chx
 * @Description: 分类展开VO
 * @DateTime: 2021/12/16 9:33
 **/
@Data
public class CategoryInfoVo {

    //所有子级分类
    private List<PmsCategory> brotherCategory;

    //当前子分类详情
    private PmsCategory currentCategory;

    //当前子分类的父类
    private PmsCategory parentCategory;
}
