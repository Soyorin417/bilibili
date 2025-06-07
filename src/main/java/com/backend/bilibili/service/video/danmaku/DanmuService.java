package com.backend.bilibili.service.video.danmaku;


import com.backend.bilibili.pojo.video.danmaku.Danmu;

import java.util.List;

public interface DanmuService {
    List<Danmu> getDanmuByVideoId(Long videoId);
    public void addDanmu(Danmu danmu);

}
