package com.backend.bilibili.service.admin.impl;

import com.backend.bilibili.mapper.video.VideoInfoMapper;
import com.backend.bilibili.pojo.video.VideoInfo;
import com.backend.bilibili.service.admin.AdminService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private VideoInfoMapper videoInfoMapper;

    @Override
    public List<VideoInfo> getPendingVideos() {
        QueryWrapper<VideoInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "pending");
        return videoInfoMapper.selectList(queryWrapper);
    }

    @Override
    public boolean updateVideoStatus(Long id, String status) {
        UpdateWrapper<VideoInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id).set("status", status);
        return videoInfoMapper.update(null, updateWrapper) > 0;
    }
}
