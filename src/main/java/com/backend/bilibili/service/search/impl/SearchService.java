package com.backend.bilibili.service.search.impl;

import com.backend.bilibili.service.dto.UserDTO;
import com.backend.bilibili.service.dto.VideoCardDTO;

import java.util.List;

public interface SearchService {
    public List<VideoCardDTO> searchVideo(String keyword);
    public List<UserDTO> searchUser(String keyword, Long currentUid);
}
