package com.backend.bilibili.service.user.account;


import com.backend.bilibili.pojo.user.UserCollect;
import com.backend.bilibili.service.dto.UserCollectVideoDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface UserCollectService extends IService<UserCollect> {
    List<UserCollect> getUserCollects(Long userId);
    List<UserCollectVideoDTO> getUserCollectedVideos(Long userId);
    boolean addCollect(Long userId, Long videoId);
    boolean deleteCollect(Long userId, Long videoId);
    boolean updateCollect(UserCollect collect);
    UserCollect getOne(Long userId, Long videoId);
}
