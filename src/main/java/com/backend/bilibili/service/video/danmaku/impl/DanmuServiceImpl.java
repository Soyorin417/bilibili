package com.backend.bilibili.service.video.danmaku.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.backend.bilibili.mapper.video.danmaku.DanmuMapper;
import com.backend.bilibili.pojo.video.danmaku.Danmu;
import com.backend.bilibili.service.redis.RedisService;
import com.backend.bilibili.service.video.danmaku.DanmuService;
import com.backend.bilibili.utils.UserTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DanmuServiceImpl implements DanmuService {

    @Autowired
    private DanmuMapper danmuMapper;

    @Autowired
    private RedisService redisService;

    @Override
    public List<Danmu> getDanmuByVideoId(Long videoId) {
        String redisKey = "danmu:video:" + videoId;

        // 先从 Redis 中获取缓存的弹幕
        String danmuJson = redisService.getValue(redisKey);
        if (danmuJson != null && !danmuJson.isEmpty()) {
            return JSONArray.parseArray(danmuJson, Danmu.class);
        }

        // 若缓存不存在，则从数据库查询
        List<Danmu> danmus = danmuMapper.selectList(
                new QueryWrapper<Danmu>()
                        .eq("video_id", videoId)
                        .eq("status", 1)
                        .orderByAsc("time_in_video")
        );

        // 存入 Redis 缓存，设置过期时间，例如 5 分钟
        redisService.setValue(redisKey, JSON.toJSONString(danmus));
        return danmus;
    }


    @Override
    public void addDanmu(Danmu danmu) {
        danmu.setUserId(UserTokenUtil.getUid());
        danmu.setSendTime(LocalDateTime.now());
        danmu.setStatus((byte) 1);
        danmu.setUserHash(generateUserHash(danmu.getUserId()));
        danmu.setDmid(generateDmid());

        if (danmu.getFontSize() == null) danmu.setFontSize(25);
        if (danmu.getFontColor() == null || danmu.getFontColor().isEmpty()) danmu.setFontColor("#ffffff");
        if (danmu.getDuration() == null) danmu.setDuration(8.0);
        if (danmu.getFont() == null || danmu.getFont().isEmpty()) danmu.setFont("SimHei");
        if (danmu.getPool() == null) danmu.setPool(0);
        if (danmu.getIsAdvanced() == null) danmu.setIsAdvanced(false);
        if (danmu.getIsScrolling() == null) danmu.setIsScrolling(false);
        if (danmu.getPositionX() == null) danmu.setPositionX(50);
        if (danmu.getPositionY() == null) danmu.setPositionY(10);
        if (danmu.getTrack() == null) danmu.setTrack(0);

        danmuMapper.insert(danmu);

        // 清除 Redis 缓存
        String redisKey = "danmu:video:" + danmu.getVideoId();
        redisService.deleteKey(redisKey);
    }


    private String generateUserHash(Long userId) {
        try {
            String source = userId + "someSecretSalt";
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(source.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return userId.toString();
        }
    }

    private String generateDmid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
