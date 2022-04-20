package com.cy.yunyi.portal.dto;

import lombok.Data;

/**
 * @Author caihx
 * @Description: 物品得分表
 * @Date 2022/2/14
 */
@Data
public class ItemPreferencesDto {
    private Long userId;
    private Long itemId;
    private Integer score;
}
