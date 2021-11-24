package com.cy.yunyi.mapper;

import com.cy.yunyi.model.BmsRegion;
import com.cy.yunyi.model.BmsRegionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BmsRegionMapper {
    long countByExample(BmsRegionExample example);

    int deleteByExample(BmsRegionExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BmsRegion record);

    int insertSelective(BmsRegion record);

    List<BmsRegion> selectByExample(BmsRegionExample example);

    BmsRegion selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BmsRegion record, @Param("example") BmsRegionExample example);

    int updateByExample(@Param("record") BmsRegion record, @Param("example") BmsRegionExample example);

    int updateByPrimaryKeySelective(BmsRegion record);

    int updateByPrimaryKey(BmsRegion record);
}