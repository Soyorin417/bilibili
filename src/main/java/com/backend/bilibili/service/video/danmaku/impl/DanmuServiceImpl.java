package com.backend.bilibili.service.video.danmaku.impl;

import com.backend.bilibili.mapper.video.danmaku.DanmuMapper;
import com.backend.bilibili.pojo.video.danmaku.Danmu;
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

    @Override
    public List<Danmu> getDanmuByVideoId(Long videoId) {
        return danmuMapper.selectList(
                new QueryWrapper<Danmu>()
                        .eq("video_id", videoId)
                        .eq("status", 1)
                        .orderByAsc("time_in_video")
        );
    }

    @Override
    public void addDanmu(Danmu danmu) {
        // 设置用户ID和发送时间
        danmu.setUserId(UserTokenUtil.getUid());
        danmu.setSendTime(LocalDateTime.now());
        danmu.setStatus((byte) 1);

        // 生成 user_hash
        danmu.setUserHash(generateUserHash(danmu.getUserId()));

        // 生成唯一 dmid
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
