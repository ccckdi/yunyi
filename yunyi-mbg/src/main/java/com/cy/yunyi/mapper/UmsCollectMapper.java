package com.cy.yunyi.mapper;

import com.cy.yunyi.model.UmsCollect;
import com.cy.yunyi.model.UmsCollectExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UmsCollectMapper {
    long countByExample(UmsCollectExample example);

    int deleteByExample(UmsCollectExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UmsCollect record);

    int insertSelective(UmsCollect record);

    List<UmsCollect> selectByExample(UmsCollectExample example);

    UmsCollect selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UmsCollect record, @Param("example") UmsCollectExample example);

    int updateByExample(@Param("record") UmsCollect record, @Param("example") UmsCollectExample example);

    int updateByPrimaryKeySelective(UmsCollect record);

    int updateByPrimaryKey(UmsCollect record);
}