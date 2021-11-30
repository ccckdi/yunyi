package com.cy.yunyi.mapper;

import com.cy.yunyi.model.PmsCategory;
import com.cy.yunyi.model.PmsCategoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PmsCategoryMapper {
    long countByExample(PmsCategoryExample example);

    int deleteByExample(PmsCategoryExample example);

    int deleteByPrimaryKey(Long id);

    int insert(PmsCategory record);

    int insertSelective(PmsCategory record);

    List<PmsCategory> selectByExample(PmsCategoryExample example);

    PmsCategory selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") PmsCategory record, @Param("example") PmsCategoryExample example);

    int updateByExample(@Param("record") PmsCategory record, @Param("example") PmsCategoryExample example);

    int updateByPrimaryKeySelective(PmsCategory record);

    int updateByPrimaryKey(PmsCategory record);
}