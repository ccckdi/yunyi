package com.cy.yunyi.portal.recommend;

import java.io.File;
import java.util.List;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

/**
 * @Author caihx
 * @Description: 基于内容相似度的推荐引擎
 * @Date 2022/2/9
 */
public class ItemBasedRecommend {
    public List<RecommendedItem> myItemBasedRecommender(long userID, int size){
        List<RecommendedItem> recommendations = null;
        try {
            DataModel model = new FileDataModel(new File("D:/movie_preferences.txt"));//构造数据模型
            ItemSimilarity similarity = new PearsonCorrelationSimilarity(model);//计算内容相似度
            Recommender recommender = new GenericItemBasedRecommender(model, similarity);//构造推荐引擎
            recommendations = recommender.recommend(userID, size);//得到推荐结果
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return recommendations;
    }

    public static void main(String[] args) {
        List<RecommendedItem> recommendedItems =
                new ItemBasedRecommend().myItemBasedRecommender(3, 5);
        for (RecommendedItem recommendedItem : recommendedItems) {
            System.out.println(recommendedItem);
        }
    }
}
