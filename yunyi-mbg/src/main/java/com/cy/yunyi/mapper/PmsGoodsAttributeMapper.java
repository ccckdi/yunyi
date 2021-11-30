package com.cy.yunyi.mapper;

import com.cy.yunyi.model.PmsGoodsAttribute;
import com.cy.yunyi.model.PmsGoodsAttributeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PmsGoodsAttributeMapper {
    long countByExample(PmsGoodsAttributeExample example);

    int deleteByExample(PmsGoodsAttributeExample example);

    int deleteByPrimaryKey(Long id);

    int insert(PmsGoodsAttribute record);

    int insertSelective(PmsGoodsAttribute record);

    List<PmsGoodsAttribute> selectByExample(PmsGoodsAttributeExample example);

    PmsGoodsAttribute selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") PmsGoodsAttribute record, @Param("example") PmsGoodsAttributeExample example);

    int updateByExample(@Param("record") PmsGoodsAttribute record, @Param("example") PmsGoodsAttributeExample example);

    int updateByPrimaryKeySelective(PmsGoodsAttribute record);

    int updateByPrimaryKey(PmsGoodsAttribute record);
}