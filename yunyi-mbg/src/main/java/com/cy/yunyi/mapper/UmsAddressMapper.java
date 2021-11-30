package com.cy.yunyi.mapper;

import com.cy.yunyi.model.UmsAddress;
import com.cy.yunyi.model.UmsAddressExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UmsAddressMapper {
    long countByExample(UmsAddressExample example);

    int deleteByExample(UmsAddressExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UmsAddress record);

    int insertSelective(UmsAddress record);

    List<UmsAddress> selectByExample(UmsAddressExample example);

    UmsAddress selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UmsAddress record, @Param("example") UmsAddressExample example);

    int updateByExample(@Param("record") UmsAddress record, @Param("example") UmsAddressExample example);

    int updateByPrimaryKeySelective(UmsAddress record);

    int updateByPrimaryKey(UmsAddress record);
}