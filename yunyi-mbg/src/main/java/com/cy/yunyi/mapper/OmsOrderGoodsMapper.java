package com.cy.yunyi.mapper;

import com.cy.yunyi.model.OmsOrderGoods;
import com.cy.yunyi.model.OmsOrderGoodsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OmsOrderGoodsMapper {
    long countByExample(OmsOrderGoodsExample example);

    int deleteByExample(OmsOrderGoodsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(OmsOrderGoods record);

    int insertSelective(OmsOrderGoods record);

    List<OmsOrderGoods> selectByExample(OmsOrderGoodsExample example);

    OmsOrderGoods selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") OmsOrderGoods record, @Param("example") OmsOrderGoodsExample example);

    int updateByExample(@Param("record") OmsOrderGoods record, @Param("example") OmsOrderGoodsExample example);

    int updateByPrimaryKeySelective(OmsOrderGoods record);

    int updateByPrimaryKey(OmsOrderGoods record);
}