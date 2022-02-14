package com.cy.yunyi.mapper;

import com.cy.yunyi.model.RmsSearchHistory;
import com.cy.yunyi.model.RmsSearchHistoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RmsSearchHistoryMapper {
    long countByExample(RmsSearchHistoryExample example);

    int deleteByExample(RmsSearchHistoryExample example);

    int deleteByPrimaryKey(Long id);

    int insert(RmsSearchHistory record);

    int insertSelective(RmsSearchHistory record);

    List<RmsSearchHistory> selectByExample(RmsSearchHistoryExample example);

    RmsSearchHistory selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") RmsSearchHistory record, @Param("example") RmsSearchHistoryExample example);

    int updateByExample(@Param("record") RmsSearchHistory record, @Param("example") RmsSearchHistoryExample example);

    int updateByPrimaryKeySelective(RmsSearchHistory record);

    int updateByPrimaryKey(RmsSearchHistory record);
}