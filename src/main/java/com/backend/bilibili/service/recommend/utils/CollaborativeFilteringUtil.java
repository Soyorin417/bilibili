package com.backend.bilibili.service.recommend.utils;

import java.util.*;
import java.util.stream.Collectors;

public class CollaborativeFilteringUtil {

    public static List<Long> recommend(Long targetUserId,
                                       Map<Long, Map<Long, Double>> allUserRatings,
                                       int topK,
                                       int topN) {

        Map<Long, Double> targetRatings = allUserRatings.get(targetUserId);
        if (targetRatings == null || targetRatings.isEmpty()) return new ArrayList<>();

        Map<Long, Double> userSimilarities = new HashMap<>();

        // 计算相似度
        for (Map.Entry<Long, Map<Long, Double>> entry : allUserRatings.entrySet()) {
            Long otherUserId = entry.getKey();
            if (otherUserId.equals(targetUserId)) continue;

            double similarity = pearsonCorrelation(targetRatings, entry.getValue());
            if (similarity > 0) {
                userSimilarities.put(otherUserId, similarity);
            }
        }

        // 取 Top-K 相似用户
        List<Long> topSimilarUsers = userSimilarities.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(topK)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // 加权推荐未看过
        Map<Long, Double> recommendedItems = new HashMap<>();
        for (Long similarUserId : topSimilarUsers) {
            Map<Long, Double> similarRatings = allUserRatings.get(similarUserId);
            double weight = userSimilarities.get(similarUserId);

            for (Map.Entry<Long, Double> itemScore : similarRatings.entrySet()) {
                Long itemId = itemScore.getKey();
                double score = itemScore.getValue();

                if (!targetRatings.containsKey(itemId)) {
                    recommendedItems.put(itemId,
                            recommendedItems.getOrDefault(itemId, 0.0) + score * weight);
                }
            }
        }

        return recommendedItems.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(topN)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    // 皮尔逊相关系数
    private static double pearsonCorrelation(Map<Long, Double> x, Map<Long, Double> y) {
        Set<Long> commonKeys = new HashSet<>(x.keySet());
        commonKeys.retainAll(y.keySet());
        if (commonKeys.size() < 2) return 0;

        int n = commonKeys.size();
        double sumX = 0, sumY = 0, sumX2 = 0, sumY2 = 0, sumXY = 0;

        for (Long key : commonKeys) {
            double xi = x.get(key);
            double yi = y.get(key);
            sumX += xi;
            sumY += yi;
            sumX2 += xi * xi;
            sumY2 += yi * yi;
            sumXY += xi * yi;
        }

        double numerator = n * sumXY - sumX * sumY;
        double denominator = Math.sqrt((n * sumX2 - sumX * sumX) * (n * sumY2 - sumY * sumY));
        if (denominator == 0) return 0;
        return numerator / denominator;
    }
}
