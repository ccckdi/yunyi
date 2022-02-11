package com.cy.portal.service.impl;

import com.cy.portal.service.SearchHistoryService;
import com.cy.yunyi.mapper.RmsSearchHistoryMapper;
import com.cy.yunyi.model.RmsSearchHistory;
import com.cy.yunyi.model.RmsSearchHistoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author caihx
 * @Description: 搜索历史Service实现类
 * @Date 2022/2/10
 */
@Service
public class SearchHistoryServiceImpl implements SearchHistoryService {

    @Autowired
    private RmsSearchHistoryMapper searchHistoryMapper;

    @Override
    public List<RmsSearchHistory> getByUserId(Long userId) {
        RmsSearchHistoryExample example = new RmsSearchHistoryExample();
        RmsSearchHistoryExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        example.setOrderByClause("create_time desc");
        List<RmsSearchHistory> searchHistoryList = searchHistoryMapper.selectByExample(example);
        return searchHistoryList;
    }

    @Override
    public void create(RmsSearchHistory searchHistory) {
        searchHistoryMapper.insertSelective(searchHistory);
    }
}
