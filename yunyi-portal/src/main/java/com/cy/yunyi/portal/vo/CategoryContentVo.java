package com.cy.yunyi.portal.vo;

import com.cy.yunyi.model.PmsCategory;
import lombok.Data;

import java.util.List;

/**
 * @Author: chx
 * @Description: 分类信息返回封装
 * @DateTime: 2021/12/15 23:41
 **/
@Data
public class CategoryContentVo {

    //父分类列表
    private  List<PmsCategory> categoryList;

    //当前分类详情
    private PmsCategory currentCategory;

    //当前分类子分类列表
    private List<PmsCategory> currentSubCategoryList;
}
