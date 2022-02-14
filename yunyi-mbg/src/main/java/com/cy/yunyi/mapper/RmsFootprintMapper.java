package com.cy.yunyi.mapper;

import com.cy.yunyi.model.RmsFootprint;
import com.cy.yunyi.model.RmsFootprintExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RmsFootprintMapper {
    long countByExample(RmsFootprintExample example);

    int deleteByExample(RmsFootprintExample example);

    int deleteByPrimaryKey(Long id);

    int insert(RmsFootprint record);

    int insertSelective(RmsFootprint record);

    List<RmsFootprint> selectByExample(RmsFootprintExample example);

    RmsFootprint selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") RmsFootprint record, @Param("example") RmsFootprintExample example);

    int updateByExample(@Param("record") RmsFootprint record, @Param("example") RmsFootprintExample example);

    int updateByPrimaryKeySelective(RmsFootprint record);

    int updateByPrimaryKey(RmsFootprint record);
}