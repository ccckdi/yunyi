package com.cy.yunyi.mapper;

import com.cy.yunyi.model.CmsCart;
import com.cy.yunyi.model.CmsCartExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CmsCartMapper {
    long countByExample(CmsCartExample example);

    int deleteByExample(CmsCartExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CmsCart record);

    int insertSelective(CmsCart record);

    List<CmsCart> selectByExample(CmsCartExample example);

    CmsCart selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CmsCart record, @Param("example") CmsCartExample example);

    int updateByExample(@Param("record") CmsCart record, @Param("example") CmsCartExample example);

    int updateByPrimaryKeySelective(CmsCart record);

    int updateByPrimaryKey(CmsCart record);
}