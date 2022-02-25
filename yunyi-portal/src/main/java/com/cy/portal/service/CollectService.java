package com.cy.portal.service;

import com.cy.yunyi.model.UmsCollect;
import io.swagger.models.auth.In;

import java.util.List;

/**
 * @Author caihx
 * @Description: 收藏夹Service
 * @Date 2022/2/14
 */
public interface CollectService {

    List<UmsCollect> list(Long userId);

    List<UmsCollect> queryByType(Long userId, Integer type, Integer pageNum, Integer pageSize, String sort, String order);

    UmsCollect queryByTypeAndValue(Long userId, Integer type, Long valueId);

    UmsCollect getById(Long id);

    Integer addCollect(UmsCollect collect);

    Integer deleteById(Long id);

    List<UmsCollect> queryByUserId(Long userId);
}
