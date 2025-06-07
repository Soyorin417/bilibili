package com.backend.bilibili.service.search;

import com.backend.bilibili.service.dto.UserDTO;
import com.backend.bilibili.service.dto.VideoCardDTO;

import java.util.List;

public interface SearchService {
    List<VideoCardDTO> searchVideo(String keyword);
    List<UserDTO> searchUser(String keyword, Long currentUid);
}
