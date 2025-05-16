package com.backend.bilibili.service.video.impl;


import com.backend.bilibili.mapper.video.VideoActionMapper;
import com.backend.bilibili.mapper.video.VideoInfoMapper;
import com.backend.bilibili.pojo.video.VideoActionInfo;
import com.backend.bilibili.service.user.account.InfoService;
import com.backend.bilibili.service.video.VideoActionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoActionServiceImpl extends ServiceImpl<VideoActionMapper, VideoActionInfo> implements VideoActionService {

    @Autowired
    private VideoActionMapper actionMapper;

    @Autowired
    private VideoInfoMapper videoInfoMapper;

    @Autowired
    private InfoService infoService;

    @Override
    public VideoActionInfo getUserVideoAction(Long videoId, Long userUid) {
        VideoActionInfo actionInfo = actionMapper.selectByUserAndVideo(userUid, videoId);
        if (actionInfo == null) {
            // 如果没有记录，返回一个默认未操作状态的对象
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

        // 增加点赞数
        videoInfoMapper.increaseLikeCount(videoId);
        return true;
    }

    @Override
    public boolean cancelLike(Long videoId, Long userUid) {
        VideoActionInfo action = actionMapper.selectByUserAndVideo(userUid, videoId);
        if (action != null && Boolean.TRUE.equals(action.getIsLike())) {
            action.setIsLike(false);
            actionMapper.updateById(action);
            videoInfoMapper.decreaseLikeCount(videoId);
            return true;
        }
        return false;
    }

    @Override
    public boolean collectVideo(Long videoId, Long userUid) {

        VideoActionInfo action = actionMapper.selectByUserAndVideo(userUid, videoId);
        if (action == null) {
            action = new VideoActionInfo();
            action.setVideoId(videoId);
            action.setUserUid(userUid);
            action.setIsCollect(true);

            actionMapper.insert(action);
        } else {
            action.setIsCollect(true);

            System.out.println("collectVideo");
            System.out.println(action);
            actionMapper.updateById(action);
        }
        videoInfoMapper.increaseCollectCount(videoId);
        return true;
    }

    @Override
    public boolean cancelCollect(Long videoId, Long userUid) {
        VideoActionInfo action = actionMapper.selectByUserAndVideo(userUid, videoId);
        if (action != null && Boolean.TRUE.equals(action.getIsCollect())) {
            action.setIsCollect(false);
            actionMapper.updateById(action);
            videoInfoMapper.decreaseCollectCount(videoId);
            return true;
        }
        return false;
    }

    @Override
    public boolean coinVideo(Long videoId, Long userUid) {

        // 查询是否已经投过币
        VideoActionInfo action = actionMapper.selectByUserAndVideo(userUid, videoId);

        // 如果已经投币，则直接返回 false，不再处理
        if (action != null && Boolean.TRUE.equals(action.getIsCoined())) {
            return false;
        }

        // 尝试减少用户硬币
        try {
            infoService.decreaseCoin(1); // 每次投币消耗1个硬币
        } catch (RuntimeException e) {
            return false; // 硬币不足
        }

        // 更新或新增投币记录
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

        // 增加视频的投币数
        videoInfoMapper.increaseCoinCount(videoId);

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
        return true;
    }
}
