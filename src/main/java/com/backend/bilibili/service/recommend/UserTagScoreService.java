package com.backend.bilibili.service.recommend;

public interface UserTagScoreService {

    void saveOrUpdateScore(Long uid,Long tag_id,String action);

    Double getUserScoreForTag(Long userId, Long tagId);

}
