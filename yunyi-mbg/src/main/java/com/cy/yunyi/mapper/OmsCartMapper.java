package com.cy.yunyi.mapper;

import com.cy.yunyi.model.OmsCart;
import com.cy.yunyi.model.OmsCartExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OmsCartMapper {
    long countByExample(OmsCartExample example);

    int deleteByExample(OmsCartExample example);

    int deleteByPrimaryKey(Long id);

    int insert(OmsCart record);

    int insertSelective(OmsCart record);

    List<OmsCart> selectByExample(OmsCartExample example);

    OmsCart selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") OmsCart record, @Param("example") OmsCartExample example);

    int updateByExample(@Param("record") OmsCart record, @Param("example") OmsCartExample example);

    int updateByPrimaryKeySelective(OmsCart record);

    int updateByPrimaryKey(OmsCart record);
}