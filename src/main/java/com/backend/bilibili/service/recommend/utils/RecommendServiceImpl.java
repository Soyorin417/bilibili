package com.backend.bilibili.service.recommend.utils;

import com.backend.bilibili.mapper.recommend.UserTagScoreMapper;
import com.backend.bilibili.mapper.recommend.UserVideoScoreMapper;
import com.backend.bilibili.pojo.recommend.UserTagScore;
import com.backend.bilibili.pojo.recommend.UserVideoScore;
import com.backend.bilibili.service.recommend.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecommendServiceImpl implements RecommendService {
    @Autowired
    private UserTagScoreMapper userTagScoreMapper;

    @Autowired
    private UserVideoScoreMapper userVideoScoreMapper;

    public List<Long> recommendTagsForUser(Long userId, int topN) {
        // 准备所有用户的标签评分
        Map<Long, Map<Long, Double>> userRatingsMap = buildUserTagScoreMap();
        return CollaborativeFilteringUtil.recommend(userId, userRatingsMap, 5, topN);
    }

    public List<Long> recommendVideosForUser(Long userId, int topN) {
        Map<Long, Map<Long, Double>> userVideoScores = buildUserVideoScoreMap();
        return CollaborativeFilteringUtil.recommend(userId, userVideoScores, 5, topN);
    }

    private Map<Long, Map<Long, Double>> buildUserVideoScoreMap() {
        List<UserVideoScore> scores = userVideoScoreMapper.selectList(null);
        Map<Long, Map<Long, Double>> userMap = new HashMap<>();
        for (UserVideoScore uvs : scores) {
            userMap.computeIfAbsent(uvs.getUserId(), k -> new HashMap<>())
                    .put(uvs.getVideoId(), uvs.getScore());
        }
        return userMap;
    }

    private Map<Long, Map<Long, Double>> buildUserTagScoreMap() {
        List<UserTagScore> scores = userTagScoreMapper.selectList(null);
        Map<Long, Map<Long, Double>> userScoresMap = new HashMap<>();
        for (UserTagScore uts : scores) {
            userScoresMap
                    .computeIfAbsent(uts.getUserId(), k -> new HashMap<>())
                    .put(uts.getTagId(), uts.getScore());
        }
        return userScoresMap;
    }
}
