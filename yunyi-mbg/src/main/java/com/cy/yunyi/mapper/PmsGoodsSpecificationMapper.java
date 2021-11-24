package com.cy.yunyi.mapper;

import com.cy.yunyi.model.PmsGoodsSpecification;
import com.cy.yunyi.model.PmsGoodsSpecificationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PmsGoodsSpecificationMapper {
    long countByExample(PmsGoodsSpecificationExample example);

    int deleteByExample(PmsGoodsSpecificationExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(PmsGoodsSpecification record);

    int insertSelective(PmsGoodsSpecification record);

    List<PmsGoodsSpecification> selectByExample(PmsGoodsSpecificationExample example);

    PmsGoodsSpecification selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PmsGoodsSpecification record, @Param("example") PmsGoodsSpecificationExample example);

    int updateByExample(@Param("record") PmsGoodsSpecification record, @Param("example") PmsGoodsSpecificationExample example);

    int updateByPrimaryKeySelective(PmsGoodsSpecification record);

    int updateByPrimaryKey(PmsGoodsSpecification record);
}