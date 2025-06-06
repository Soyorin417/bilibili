package com.backend.bilibili.service.video.danmaku.impl;

import com.backend.bilibili.mapper.video.danmaku.DanmakuMapper;
import com.backend.bilibili.pojo.video.danmaku.Danmaku;
import com.backend.bilibili.service.video.danmaku.DanmakuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

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
    @Override
    public boolean addDanmaku(Danmaku danmaku) {
        return danmakuMapper.insert(danmaku) > 0;
    }

    @Override
    public boolean updateDanmaku(Danmaku danmaku) {
        return false;
    }

    @Override
    public boolean deleteDanmaku(int id) {
        return false;
    }


    @Override
    public String fetchDanmakuXml(String oid) throws IOException {
        String urlStr = "https://api.bilibili.com/x/v1/dm/list.so?oid=" + oid;
        HttpURLConnection connection = null;

        try {
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
            connection.setRequestProperty("Accept-Encoding", "deflate");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();

            InputStream rawStream = connection.getInputStream();
            InputStream inputStream = new InflaterInputStream(rawStream, new Inflater(true));

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                StringBuilder xmlBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    xmlBuilder.append(line).append("\n");
                }
                return xmlBuilder.toString();
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
