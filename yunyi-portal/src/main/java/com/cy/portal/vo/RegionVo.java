package com.cy.portal.vo;

import lombok.Data;

import java.util.List;

/**
 * 行政区表VO
 */
@Data
public class RegionVo {
    private Integer id;
    private String name;
    private Byte type;
    private Integer code;
    private List<RegionVo> children;
}
