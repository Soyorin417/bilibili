package com.backend.bilibili.service.video.impl;

import com.backend.bilibili.mapper.video.VideoInfoMapper;
import com.backend.bilibili.pojo.video.VideoInfo;
import com.backend.bilibili.service.video.VideoInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VideoInfoServiceImpl implements VideoInfoService {

    @Autowired
    private VideoInfoMapper videoInfoMapper;

    @Override
    public VideoInfo getVideoInfoById(int id) {

        try {
            QueryWrapper<VideoInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", id);
            VideoInfo videoInfo = videoInfoMapper.selectOne(queryWrapper);
            if (videoInfo == null) {
                throw new RuntimeException("id不存在");
            }
            System.out.println("START");
            return videoInfo;
        } catch (Exception e) {
            System.err.println("数据库查询异常：" + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("数据库连接异常，请稍后再试");
        }
    }



    @Override
    public List<VideoInfo> getAllVideos() {
        return videoInfoMapper.selectList(null);
    }
}
