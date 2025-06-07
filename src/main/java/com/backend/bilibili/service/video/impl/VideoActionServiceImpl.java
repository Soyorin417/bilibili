package com.backend.bilibili.service.video.impl;


import com.backend.bilibili.mapper.video.VideoActionMapper;
import com.backend.bilibili.mapper.video.VideoInfoMapper;
import com.backend.bilibili.pojo.user.UserCollect;
import com.backend.bilibili.pojo.video.VideoActionInfo;
import com.backend.bilibili.service.recommend.UserTagScoreService;
import com.backend.bilibili.service.recommend.UserVideoScoreService;
import com.backend.bilibili.service.user.account.InfoService;
import com.backend.bilibili.service.user.account.UserCollectService;
import com.backend.bilibili.service.video.VideoActionService;
import com.backend.bilibili.service.video.VideoTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoActionServiceImpl extends ServiceImpl<VideoActionMapper, VideoActionInfo> implements VideoActionService {

    @Autowired
    private VideoActionMapper actionMapper;

    @Autowired
    private VideoInfoMapper videoInfoMapper;

    @Autowired
    private VideoTagService videoTagService;

    @Autowired
    private InfoService infoService;

    @Autowired
    private UserCollectService userCollectService;

    @Autowired
    private UserTagScoreService userTagScoreService;

    @Autowired
    private UserVideoScoreService userVideoScoreService;

    @Override
    public VideoActionInfo getUserVideoAction(Long videoId, Long userUid) {
        VideoActionInfo actionInfo = actionMapper.selectByUserAndVideo(userUid, videoId);
        if (actionInfo == null) {
            actionInfo = new VideoActionInfo();
            actionInfo.setVideoId(videoId);
            actionInfo.setUserUid(userUid);
            actionInfo.setIsLike(false);
            actionInfo.setIsCollect(false);
            actionInfo.setIsCoined(false);
            actionInfo.setIsShare(false);
        }
        return actionInfo;
    }

    @Override
    public boolean likeVideo(Long videoId, Long userUid) {
        VideoActionInfo action = actionMapper.selectByUserAndVideo(userUid, videoId);
        if (action != null && Boolean.TRUE.equals(action.getIsLike())) {
            return false; // 已点赞
        }
        if (action == null) {
            action = new VideoActionInfo();
            action.setUserUid(userUid);
            action.setVideoId(videoId);
            action.setIsLike(true);
            actionMapper.insert(action);
        } else {
            action.setIsLike(true);
            actionMapper.updateById(action);
        }

        videoInfoMapper.increaseLikeCount(videoId);

        // 加分
        userVideoScoreService.saveOrUpdateScore(userUid, videoId, "like");
        List<Long> tagIds = videoTagService.getTagIdsByVideoId(videoId);
        for (Long tagId : tagIds) {
            userTagScoreService.saveOrUpdateScore(userUid, tagId, "like");
        }
        return true;
    }

    @Override
    public boolean cancelLike(Long videoId, Long userUid) {
        VideoActionInfo action = actionMapper.selectByUserAndVideo(userUid, videoId);
        if (action != null && Boolean.TRUE.equals(action.getIsLike())) {
            action.setIsLike(false);
            actionMapper.updateById(action);
            videoInfoMapper.decreaseLikeCount(videoId);

            // 扣分
            userVideoScoreService.saveOrUpdateScore(userUid, videoId, "unlike");
            List<Long> tagIds = videoTagService.getTagIdsByVideoId(videoId);
            for (Long tagId : tagIds) {
                userTagScoreService.saveOrUpdateScore(userUid, tagId, "unlike");
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean collectVideo(Long videoId, Long userUid) {
        UserCollect exist = userCollectService.getOne(userUid, videoId);
        if (exist != null) {
            return false; // 已收藏
        }
        VideoActionInfo action = actionMapper.selectByUserAndVideo(userUid, videoId);
        if (action == null) {
            action = new VideoActionInfo();
            action.setVideoId(videoId);
            action.setUserUid(userUid);
            action.setIsCollect(true);
            actionMapper.insert(action);
        } else {
            action.setIsCollect(true);
            actionMapper.updateById(action);
        }
        userCollectService.addCollect(userUid, videoId);
        videoInfoMapper.increaseCollectCount(videoId);

        // 加分
        userVideoScoreService.saveOrUpdateScore(userUid, videoId, "collect");
        List<Long> tagIds = videoTagService.getTagIdsByVideoId(videoId);
        for (Long tagId : tagIds) {
            userTagScoreService.saveOrUpdateScore(userUid, tagId, "collect");
        }
        return true;
    }

    @Override
    public boolean cancelCollect(Long videoId, Long userUid) {
        UserCollect exist = userCollectService.getOne(userUid, videoId);
        if (exist == null) {
            return false; // 无收藏记录
        }
        userCollectService.deleteCollect(userUid, videoId);

        VideoActionInfo action = actionMapper.selectByUserAndVideo(userUid, videoId);
        if (action != null && Boolean.TRUE.equals(action.getIsCollect())) {
            action.setIsCollect(false);
            actionMapper.updateById(action);
            videoInfoMapper.decreaseCollectCount(videoId);

            // 扣分
            userVideoScoreService.saveOrUpdateScore(userUid, videoId, "uncollected");
            List<Long> tagIds = videoTagService.getTagIdsByVideoId(videoId);
            for (Long tagId : tagIds) {
                userTagScoreService.saveOrUpdateScore(userUid, tagId, "uncollected");
            }
        }
        return true;
    }

    @Override
    public boolean coinVideo(Long videoId, Long userUid) {
        VideoActionInfo action = actionMapper.selectByUserAndVideo(userUid, videoId);
        if (action != null && Boolean.TRUE.equals(action.getIsCoined())) {
            return false; // 已投币
        }
        try {
            infoService.decreaseCoin(1);
        } catch (RuntimeException e) {
            return false; // 硬币不足
        }
        if (action == null) {
            action = new VideoActionInfo();
            action.setVideoId(videoId);
            action.setUserUid(userUid);
            action.setIsCoined(true);
            actionMapper.insert(action);
        } else {
            action.setIsCoined(true);
            actionMapper.updateById(action);
        }
        videoInfoMapper.increaseCoinCount(videoId);

        // 投币加分（取消投币不扣分，业务规定）
        userVideoScoreService.saveOrUpdateScore(userUid, videoId, "coin");
        List<Long> tagIds = videoTagService.getTagIdsByVideoId(videoId);
        for (Long tagId : tagIds) {
            userTagScoreService.saveOrUpdateScore(userUid, tagId, "coin");
        }
        return true;
    }

    @Override
    public boolean cancelCoin(Long videoId, Long userUid) {
        VideoActionInfo action = actionMapper.selectByUserAndVideo(userUid, videoId);
        if (action != null && Boolean.TRUE.equals(action.getIsCoined())) {
            action.setIsCoined(false);
            actionMapper.updateById(action);
            videoInfoMapper.decreaseCoinCount(videoId);
            return true;
        }
        return false;
    }

    @Override
    public boolean shareVideo(Long videoId, Long userUid) {
        VideoActionInfo action = actionMapper.selectByUserAndVideo(userUid, videoId);
        if (action == null) {
            action = new VideoActionInfo();
            action.setVideoId(videoId);
            action.setUserUid(userUid);
            action.setIsShare(true);
            actionMapper.insert(action);
        } else {
            action.setIsShare(true);
            actionMapper.updateById(action);
        }
        videoInfoMapper.increaseShareCount(videoId);

        // 加分
        userVideoScoreService.saveOrUpdateScore(userUid, videoId, "share");
        List<Long> tagIds = videoTagService.getTagIdsByVideoId(videoId);
        for (Long tagId : tagIds) {
            userTagScoreService.saveOrUpdateScore(userUid, tagId, "share");
        }
        return true;
    }

}

