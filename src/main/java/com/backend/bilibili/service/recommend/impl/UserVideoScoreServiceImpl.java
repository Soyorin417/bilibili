package com.backend.bilibili.service.recommend.impl;

import com.backend.bilibili.mapper.recommend.UserVideoScoreMapper;
import com.backend.bilibili.pojo.recommend.UserVideoScore;
import com.backend.bilibili.service.recommend.UserVideoScoreService;
import com.backend.bilibili.service.recommend.cofig.BehaviorWeights;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserVideoScoreServiceImpl implements UserVideoScoreService {

    @Autowired
    private UserVideoScoreMapper userVideoScoreMapper;

    @Override
    public void saveOrUpdateScore(Long uid, Long videoId, String action) {
        double scoreIncrement = BehaviorWeights.getScoreByAction(action);
        if (scoreIncrement == 0) {
            System.out.println("未知行为：" + action);
            return;
        }

        QueryWrapper<UserVideoScore> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", uid).eq("video_id", videoId);

        UserVideoScore existing = userVideoScoreMapper.selectOne(wrapper);
        if (existing != null) {
            existing.setScore(existing.getScore() + scoreIncrement);
            existing.setUpdateTime(LocalDateTime.now());
            userVideoScoreMapper.updateById(existing);
        } else {
            UserVideoScore score = new UserVideoScore();
            score.setUserId(uid);
            score.setVideoId(videoId);
            score.setScore(scoreIncrement);
            score.setUpdateTime(LocalDateTime.now());
            userVideoScoreMapper.insert(score);
        }
    }

    @Override
    public Double getUserScoreForVideo(Long uid, Long videoId) {
        QueryWrapper<UserVideoScore> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", uid).eq("video_id", videoId);
        UserVideoScore result = userVideoScoreMapper.selectOne(wrapper);
        return result != null ? result.getScore() : null;
    }
}
