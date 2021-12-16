package com.cy.yunyi.mapper;

import com.cy.yunyi.model.BmsIssue;
import com.cy.yunyi.model.BmsIssueExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BmsIssueMapper {
    long countByExample(BmsIssueExample example);

    int deleteByExample(BmsIssueExample example);

    int deleteByPrimaryKey(Long id);

    int insert(BmsIssue record);

    int insertSelective(BmsIssue record);

    List<BmsIssue> selectByExample(BmsIssueExample example);

    BmsIssue selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") BmsIssue record, @Param("example") BmsIssueExample example);

    int updateByExample(@Param("record") BmsIssue record, @Param("example") BmsIssueExample example);

    int updateByPrimaryKeySelective(BmsIssue record);

    int updateByPrimaryKey(BmsIssue record);
}