package com.cy.yunyi.portal.service.impl;

import com.cy.yunyi.portal.service.RegionService;
import com.cy.yunyi.portal.vo.RegionVo;
import com.cy.yunyi.mapper.BmsRegionMapper;
import com.cy.yunyi.model.BmsRegion;
import com.cy.yunyi.model.BmsRegionExample;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: chx
 * @Description: 区域信息Service实现类
 * @DateTime: 2021/12/28 21:05
 **/
@Service
public class RegionServiceImpl implements RegionService {
    
    @Autowired
    private BmsRegionMapper regionMapper;
    
    @Override
    public List<RegionVo> list() {
        BmsRegionExample example = new BmsRegionExample();
        List<BmsRegion> regionList = regionMapper.selectByExample(example);
        List<RegionVo> regionVoList = regionList.stream()
                .filter(region -> region.getPid().equals(0L))
                .map(region -> covertregionNode(region, regionList)).collect(Collectors.toList());
        return regionVoList;
    }

    /***
     * 树状结构数据处理
     */
    private RegionVo covertregionNode(BmsRegion region, List<BmsRegion> regionList) {
        RegionVo regionVo = new RegionVo();
        BeanUtils.copyProperties(region, regionVo);
        List<RegionVo> children = regionList.stream()
                .filter(subregion -> subregion.getPid().equals(region.getId()))
                .map(subMenu -> covertregionNode(subMenu, regionList)).collect(Collectors.toList());
        regionVo.setChildren(children);
        return regionVo;
    }
}
