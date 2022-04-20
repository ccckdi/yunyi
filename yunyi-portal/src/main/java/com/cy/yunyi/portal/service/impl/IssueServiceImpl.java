package com.cy.yunyi.portal.service.impl;

import com.cy.yunyi.portal.service.IssueService;
import com.cy.yunyi.mapper.BmsIssueMapper;
import com.cy.yunyi.model.BmsIssue;
import com.cy.yunyi.model.BmsIssueExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: chx
 * @Description: 常见问题Service实现类
 * @DateTime: 2021/12/16 15:23
 **/
@Service
public class IssueServiceImpl implements IssueService {

    @Autowired
    private BmsIssueMapper issueMapper;

    @Override
    public List<BmsIssue> list() {
        return issueMapper.selectByExample(new BmsIssueExample());
    }
}
