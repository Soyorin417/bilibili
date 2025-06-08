package com.backend.bilibili.service.profile;

import com.backend.bilibili.service.dto.VideoCardDTO;

import java.util.List;

public interface SpaceService {
    List<VideoCardDTO> getUserPublishedVideos(long userId);
}
