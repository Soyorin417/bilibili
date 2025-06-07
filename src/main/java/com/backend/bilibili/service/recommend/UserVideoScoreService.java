package com.backend.bilibili.service.recommend;

public interface UserVideoScoreService {
    void saveOrUpdateScore(Long uid, Long videoId, String action);
    Double getUserScoreForVideo(Long uid, Long videoId);
}
