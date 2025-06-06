package com.backend.bilibili.service.video.danmaku;


import com.backend.bilibili.pojo.video.danmaku.Danmaku;

import java.io.IOException;

public interface DanmakuService {
    Danmaku getDanmaku(int id);
    String fetchDanmakuXml(String oid) throws IOException;

    boolean addDanmaku(Danmaku danmaku);

    boolean updateDanmaku(Danmaku danmaku);

    boolean deleteDanmaku(int id);
}
