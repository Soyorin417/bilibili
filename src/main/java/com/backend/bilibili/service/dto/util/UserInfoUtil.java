package com.backend.bilibili.service.dto.util;

import com.backend.bilibili.pojo.user.UserInfo;

import java.util.HashMap;
import java.util.Map;


public class UserInfoUtil {
    public static Map<String, Object> convertToMap(UserInfo user) {
        Map<String, Object> map = new HashMap<>();
        map.put("error_message", "success");
        map.put("id", user.getUid());
        map.put("username", user.getUsername());
        map.put("avatar", user.getAvatar());
        map.put("level", user.getLevel());
        map.put("exp", user.getExp());
        map.put("coin", user.getCoin());
        map.put("bcoin", user.getBcoin());
        map.put("fans", user.getFans());
        map.put("dynamic", user.getDynamic());
        map.put("role", user.getRole());
        map.put("register_time", user.getRegisterTime());
        map.put("is_banned", user.getIsBanned());
        map.put("biography", user.getBiography());
        return map;
    }
    public static UserInfo convertToUserInfo(Map<String, Object> userInfoMap) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername((String) userInfoMap.get("username"));
        userInfo.setAvatar((String) userInfoMap.get("avatar"));
        userInfo.setLevel((Integer) userInfoMap.getOrDefault("level", 1));
        userInfo.setExp((Integer) userInfoMap.getOrDefault("exp", 0));
        userInfo.setCoin((Integer) userInfoMap.getOrDefault("coin", 0));
        userInfo.setBcoin((Integer) userInfoMap.getOrDefault("bcoin", 0));
        userInfo.setFans((Integer) userInfoMap.getOrDefault("fans", 0));
        userInfo.setDynamic((Integer) userInfoMap.getOrDefault("dynamic", 0));
        userInfo.setRole((String) userInfoMap.getOrDefault("role", "user"));
        return userInfo;
    }



}
