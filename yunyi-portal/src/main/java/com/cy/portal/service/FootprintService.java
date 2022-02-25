package com.cy.portal.service;

import com.cy.yunyi.model.RmsFootprint;

import java.util.List;

/**
 * @Author caihx
 * @Description: 用户足迹Service
 * @Date 2022/2/9
 */
public interface FootprintService {
    void create(RmsFootprint footprint);

    List<RmsFootprint> list(Long userId, Integer pageNum, Integer pageSize, String sort, String order);

    List<RmsFootprint> queryByUserId(Long userId);
}
