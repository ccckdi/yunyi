package com.cy.yunyi.portal.service.impl;

import com.cy.yunyi.portal.service.CollectService;
import com.cy.yunyi.mapper.UmsCollectMapper;
import com.cy.yunyi.model.UmsCollect;
import com.cy.yunyi.model.UmsCollectExample;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @Author caihx
 * @Description: 收藏夹Service实现类
 * @Date 2022/2/14
 */
@Service
public class CollectServiceImpl implements CollectService {

    @Autowired
    private UmsCollectMapper collectMapper;

    @Override
    public List<UmsCollect> list(Long userId) {
        UmsCollectExample example = new UmsCollectExample();
        UmsCollectExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andStatusEqualTo(1);
        List<UmsCollect> collectList = collectMapper.selectByExample(example);
        return collectList;
    }

    @Override
    public List<UmsCollect> queryByType(Long userId, Integer type, Integer pageNum, Integer pageSize, String sort, String order) {
        PageHelper.startPage(pageNum,pageSize);
        UmsCollectExample example = new UmsCollectExample();
        UmsCollectExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andTypeEqualTo(type);
        criteria.andStatusEqualTo(1);

        //排序
        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        List<UmsCollect> collectList = collectMapper.selectByExample(example);
        return collectList;
    }

    @Override
    public UmsCollect queryByTypeAndValue(Long userId, Integer type, Long valueId) {
        UmsCollectExample example = new UmsCollectExample();
        UmsCollectExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andValueIdEqualTo(valueId);
        criteria.andTypeEqualTo(type);
        criteria.andStatusEqualTo(1);


        List<UmsCollect> collectList = collectMapper.selectByExample(example);
        if (collectList.size() > 0){
            return collectList.get(0);
        }
        return null;
    }

    @Override
    public UmsCollect getById(Long id) {
        UmsCollectExample example = new UmsCollectExample();
        UmsCollectExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);
        criteria.andStatusEqualTo(1);
        List<UmsCollect> collectList = collectMapper.selectByExample(example);
        if (collectList.size() > 0){
            return collectList.get(0);
        }
        return null;
    }

    @Override
    public Integer addCollect(UmsCollect collect) {
        collect.setCreateTime(new Date());
        collect.setStatus(1);
        int result = collectMapper.insert(collect);
        return result;
    }

    @Override
    public Integer deleteById(Long id) {
        int result = collectMapper.deleteByPrimaryKey(id);
        return result;
    }

    @Override
    public List<UmsCollect> queryByUserId(Long userId) {
        UmsCollectExample example = new UmsCollectExample();
        UmsCollectExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andStatusEqualTo(1);
        List<UmsCollect> collectList = collectMapper.selectByExample(example);
        return collectList;
    }
}
