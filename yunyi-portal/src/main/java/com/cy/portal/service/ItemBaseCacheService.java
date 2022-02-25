package com.cy.portal.service;

import com.cy.portal.dto.ItemPreferencesDto;
import com.cy.yunyi.common.service.RedisService;
import com.cy.yunyi.model.PmsGoods;
import com.cy.yunyi.model.UmsAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

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
