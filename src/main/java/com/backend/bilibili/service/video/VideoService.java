package com.backend.bilibili.service.video;

import com.backend.bilibili.pojo.video.VideoCardDTO;

import java.util.List;

public interface VideoService {
    List<VideoCardDTO> getVideoCardList();
}
