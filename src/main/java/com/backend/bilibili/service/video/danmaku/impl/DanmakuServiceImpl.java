package com.backend.bilibili.service.video.danmaku.impl;

import com.backend.bilibili.mapper.video.danmaku.DanmakuMapper;
import com.backend.bilibili.pojo.video.danmaku.Danmaku;
import com.backend.bilibili.service.video.danmaku.DanmakuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DanmakuServiceImpl implements DanmakuService {
    @Autowired
    private DanmakuMapper danmakuMapper;



    @Override
    public Danmaku getDanmaku(int id) {
        QueryWrapper<Danmaku> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("vid", id);
        return danmakuMapper.selectOne(queryWrapper);
    }
}
