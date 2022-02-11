package com.cy.portal.service;

import com.cy.yunyi.model.RmsSearchHistory;

import java.util.List;

/**
 * @Author caihx
 * @Description: 搜索历史Service
 * @Date 2022/2/10
 */
public interface SearchHistoryService {

    List<RmsSearchHistory> getByUserId(Long userId);

    void create(RmsSearchHistory searchHistory);
}
