package com.cy.portal.recommend;

import com.cy.portal.dto.ItemPreferencesDto;
import com.cy.portal.service.*;
import com.cy.yunyi.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author caihx
 * @Description: 根据用户浏览、收藏、购买、计算物品得分
 * @Date 2022/2/11
 */
@Component
public class CalculateItemScore {

    @Autowired
    private FootprintService footprintService;

    @Autowired
    private CollectService collectService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private ItemBaseCacheService itemBaseCacheService;
    /**
     * 浏览 1分
     * 收藏 2分
     * 购买 3分
     */
    public List<PmsGoods> recommendByUserId(Long userId){
        //读取推荐得分
        List<ItemPreferencesDto> scoreList = itemBaseCacheService.getScore(userId);
        scoreList = scoreList.stream().sorted(Comparator.comparing(ItemPreferencesDto::getScore).reversed()).collect(Collectors.toList());
        //每个类型商品的权重
        int[] weight = {4,3,2};
        //推荐总数
        int size = 9;
        //推荐集合
        List<PmsGoods> recommendList = new ArrayList<>(size);
        //进行推荐
        for (int i = 0; i < scoreList.size(); i++) {
            ItemPreferencesDto itemPreferencesDto = scoreList.get(i);
            Long itemId = itemPreferencesDto.getItemId();
            PmsGoods goods = goodsService.getById(itemId);
            Long categoryId = goods.getCategoryId();
            int w = weight[i];
            List<PmsGoods> goodsList = goodsService.recommendByCategoryId(itemId,categoryId);
            for (int j = 0; j < goodsList.size(); j++) {
                recommendList.add(goodsList.get(i));
                if (i == w){
                    break;
                }
            }
        }
        return recommendList;
    }

    public void computeResultByUserId(Long userId){
        //{商品id,得分}
        Map<Long,Integer> score = new HashMap<>();
        //读取用户浏览数据
        List<RmsFootprint> footprintList = footprintService.queryByUserId(userId);
        //读取用户收藏数据
        List<UmsCollect> collectList = collectService.queryByUserId(userId);
        //读取用户订单数据
        List<OmsOrder> orderList = orderService.queryByUserId(userId);
        //计算得分
        for (RmsFootprint footprint : footprintList) {
            Long goodsId = footprint.getGoodsId();
            if (score.containsKey(goodsId)){
                score.put(goodsId,score.get(goodsId) + 1);
            }else {
                score.put(goodsId,1);
            }
        }
        for (UmsCollect collect : collectList) {
            Long goodsId = collect.getUserId();
            if (score.containsKey(goodsId)){
                score.put(goodsId,score.get(goodsId) + 2);
            }else {
                score.put(goodsId,2);
            }
        }
        for (OmsOrder order : orderList) {
            Long goodsId = order.getUserId();
            if (score.containsKey(goodsId)){
                score.put(goodsId,score.get(goodsId) + 3);
            }else {
                score.put(goodsId,2);
            }
        }
        //写入redis
        for (Long goodsId : score.keySet()) {
            ItemPreferencesDto itemPreferencesDto = new ItemPreferencesDto();
            itemPreferencesDto.setUserId(userId);
            itemPreferencesDto.setItemId(goodsId);
            itemPreferencesDto.setScore(score.get(goodsId));
            itemBaseCacheService.putScore(itemPreferencesDto);
        }
    }
}
