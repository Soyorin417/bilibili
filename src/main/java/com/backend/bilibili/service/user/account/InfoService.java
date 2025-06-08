package com.backend.bilibili.service.user.account;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


public interface InfoService {
    Map<String, Object> decreaseCoin(int amount);
    Map<String, Object> getCurrentUserInfo();
    Map<String, Object> getUserByUid(Long uid);
    Map<String, Object> updateUser(Map<String, Object> updateInfo);
    Map<String, Object> deleteUser(Long uid);
    Map<String, Object> updateUserInfoWithAvatar(MultipartFile avatarFile, Map<String, Object> updateInfo);
    Map<String, Object> getUserInfoByUserName(String username);
    List<Map<String, Object>> getAllUserInfo();
    Map<String, Object> getUserInfoById(Long uid);
    Long getAllUserInfoCount();
    boolean banUser(Long uid);
    boolean unbanUser(Long uid);
    Long getUserFansCount(Long uid);
    String getUserNameById(Long uid);
}
