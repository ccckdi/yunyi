package com.cy.yunyi.portal.service;

import com.cy.yunyi.portal.dto.ItemPreferencesDto;

import java.util.List;

/**
 * @Author caihx
 * @Description: 推荐服务缓存Service
 * @Date 2022/2/14
 */
public interface ItemBaseCacheService {

    /**
     * 删除得分信息缓存
     */
    void delScore(Long itemId);

    /**
     * 获取得分信息
     */
    List<ItemPreferencesDto> getScore(Long itemId);

    /**
     * 设置得分信息缓存
     */
    void putScore(ItemPreferencesDto ItemPreferencesDto);
}
