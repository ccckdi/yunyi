package com.cy.yunyi.mapper;

import com.cy.yunyi.model.PmsGoodsProduct;
import com.cy.yunyi.model.PmsGoodsProductExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PmsGoodsProductMapper {
    long countByExample(PmsGoodsProductExample example);

    int deleteByExample(PmsGoodsProductExample example);

    int deleteByPrimaryKey(Long id);

    int insert(PmsGoodsProduct record);

    int insertSelective(PmsGoodsProduct record);

    List<PmsGoodsProduct> selectByExample(PmsGoodsProductExample example);

    PmsGoodsProduct selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") PmsGoodsProduct record, @Param("example") PmsGoodsProductExample example);

    int updateByExample(@Param("record") PmsGoodsProduct record, @Param("example") PmsGoodsProductExample example);

    int updateByPrimaryKeySelective(PmsGoodsProduct record);

    int updateByPrimaryKey(PmsGoodsProduct record);
}