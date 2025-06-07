package com.backend.bilibili.service.recommend.impl;

import com.backend.bilibili.mapper.recommend.UserTagScoreMapper;
import com.backend.bilibili.pojo.recommend.UserTagScore;
import com.backend.bilibili.service.recommend.UserTagScoreService;
import com.backend.bilibili.service.recommend.cofig.BehaviorWeights;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserTagScoreServiceImpl implements UserTagScoreService {

    @Autowired
    private UserTagScoreMapper userTagScoreMapper;

    @Override
    public void saveOrUpdateScore(Long uid, Long tagId, String action) {
        double scoreIncrement = BehaviorWeights.getScoreByAction(action); // 使用传入的行为
        if (scoreIncrement == 0) {
            System.out.println("未知行为：" + action);
            return;
        }

        QueryWrapper<UserTagScore> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", uid).eq("tag_id", tagId);

        UserTagScore existing = userTagScoreMapper.selectOne(wrapper);
        if (existing != null) {
            existing.setScore(existing.getScore() + scoreIncrement);
            existing.setUpdateTime(LocalDateTime.now());
            userTagScoreMapper.updateById(existing);
        } else {
            UserTagScore score = new UserTagScore();
            score.setUserId(uid);
            score.setTagId(tagId);
            score.setScore(scoreIncrement);
            score.setUpdateTime(LocalDateTime.now());
            userTagScoreMapper.insert(score);
        }
    }

    @Override
    public Double getUserScoreForTag(Long userId, Long tagId) {
        QueryWrapper<UserTagScore> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("tag_id", tagId);
        UserTagScore result = userTagScoreMapper.selectOne(wrapper);
        return result != null ? result.getScore() : null;
    }
}

