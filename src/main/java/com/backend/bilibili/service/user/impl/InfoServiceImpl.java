package com.backend.bilibili.service.user.impl;

import com.backend.bilibili.mapper.user.UserInfoMapper;
import com.backend.bilibili.pojo.user.UserInfo;
import com.backend.bilibili.service.minio.MinioService;
import com.backend.bilibili.service.redis.RedisService;
import com.backend.bilibili.service.user.account.InfoService;
import com.backend.bilibili.service.user.utils.UserInfoUtil;
import com.backend.bilibili.utils.UserTokenUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InfoServiceImpl implements InfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private MinioService minioService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserTokenUtil userTokenUtil;




    //更新头像
    @Override
    public Map<String, Object> updateUserInfoWithAvatar(MultipartFile avatarFile, Map<String, Object> updateInfo) {
        UserInfo userInfo = UserTokenUtil.getCurrentUser();
        if (userInfo == null) {
            return null;
        }

        // 上传头像
        if (avatarFile != null && !avatarFile.isEmpty()) {
            try {
                String avatarUrl = minioService.putObject(avatarFile);  // 上传
                userInfo.setAvatar(avatarUrl);
            } catch (Exception e) {
                throw new RuntimeException("头像上传失败: " + e.getMessage());
            }
        }

        userInfoMapper.updateById(userInfo);

        Map<String, Object> map = new HashMap<>();
        map.put("error_message", "success");
        map.put("avatar", userInfo.getAvatar());
        return map;
    }

    @Override
    public Map<String, Object> getUserInfoByUserName(String username) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        UserInfo user = userInfoMapper.selectOne(queryWrapper);

        if (user == null) {
            return Collections.emptyMap();
        }
        return UserInfoUtil.convertToMap(user);
    }

    @Override
    public Map<String, Object> getUserInfoById(Long uid) {
        UserInfo user = userInfoMapper.selectOne(new QueryWrapper<UserInfo>().eq("uid", uid));
        if (user == null) {
            return Collections.emptyMap();
        }
        return UserInfoUtil.convertToMap(user);
    }



    @Override
    public List<Map<String, Object>> getAllUserInfo() {
        Long uid = UserTokenUtil.getCurrentUser().getUid();

        List<UserInfo> users = userInfoMapper.selectList(null);
        return users.stream()
                .filter(user -> !user.getUid().equals(uid))  // 过滤掉自己
                .map(UserInfoUtil::convertToMap)
                .collect(Collectors.toList());
    }



    //获取用户数
    @Override
    public Long getAllUserInfoCount() {
        return userInfoMapper.selectCount(null);
    }

    @Override
    public boolean banUser(Long uid) {
        UpdateWrapper<UserInfo> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", uid)
                .set("is_banned", true);
        return userInfoMapper.update(null, wrapper) > 0;
    }

    @Override
    public boolean unbanUser(Long uid) {
        UpdateWrapper<UserInfo> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", uid)
                .set("is_banned", false);
        return userInfoMapper.update(null, wrapper) > 0;
    }

    @Override
    public Long getUserFansCount(Long uid) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid);
        return userInfoMapper.selectCount(queryWrapper);
    }

    // 获取当前登录用户信息
    @Override
    public Map<String, Object> getCurrentUserInfo() {
        Long uid = UserTokenUtil.getUid();
        String redisKey = "user:info:" + uid;

        // 从 Redis 取缓存
        String cachedJson = redisService.getValue(redisKey);
        if (cachedJson != null && !cachedJson.isEmpty()) {
            try {
                Map cachedMap = objectMapper.readValue(cachedJson, Map.class);
                System.out.println("从Redis缓存获取用户信息");
                System.out.println(cachedMap);
                return cachedMap;
            } catch (Exception e) {
                System.out.println("缓存数据反序列化失败，准备查询数据库");
            }
        }

        // 缓存未命中，从数据库查询
        UserInfo userInfo = UserTokenUtil.getCurrentUser();
        if (userInfo == null) {
            return null;
        }

        Map<String, Object> map = UserInfoUtil.convertToMap(userInfo);

        try {
            // 把数据写回 Redis 缓存，方便下次读取
            String jsonToCache = objectMapper.writeValueAsString(map);
            redisService.setValue(redisKey, jsonToCache);
        } catch (Exception e) {
            System.out.println("写入缓存失败：" + e.getMessage());
        }

        return map;
    }



    // 根据 uid 获取指定用户信息
    @Override
    public Map<String, Object> getUserByUid(Long uid) {
        UserInfo userInfo = userInfoMapper.selectById(uid);
        Map<String, Object> map = new HashMap<>();
        if (userInfo == null) {
            map.put("error_message", "用户不存在");
        } else {
            map = UserInfoUtil.convertToMap(userInfo);
        }
        return map;
    }

    // 更新当前登录用户信息
    @Override
    public Map<String, Object> updateUser(Map<String, Object> updateInfo) {
        UserInfo userInfo = UserTokenUtil.getCurrentUser();
        if (userInfo == null) {
            return null;
        }

        // 根据需要判断字段更新
        userInfo.setAvatar((String) updateInfo.getOrDefault("avatar", userInfo.getAvatar()));

        userInfoMapper.updateById(userInfo);

        Map<String, Object> map = new HashMap<>();
        map.put("error_message", "success");
        return map;
    }

    // 删除指定用户
    @Override
    public Map<String, Object> deleteUser(Long uid) {
        int result = userInfoMapper.deleteById(uid);
        Map<String, Object> map = new HashMap<>();
        if (result > 0) {
            map.put("error_message", "success");
        } else {
            map.put("error_message", "删除失败，用户不存在");
        }
        return map;
    }

    // 扣除当前登录用户硬币
    @Override
    public Map<String, Object> decreaseCoin(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("减少金额必须大于0");
        }

        UserInfo userInfo = UserTokenUtil.getCurrentUser();
        if (userInfo == null) {
            return null;
        }

        if (userInfo.getCoin() < amount) {
            throw new RuntimeException("硬币不足");
        }

        userInfo.setCoin(userInfo.getCoin() - amount);
        userInfoMapper.updateById(userInfo);

        Map<String, Object> result = new HashMap<>();
        result.put("error_message", "success");
        result.put("remaining_coin", userInfo.getCoin());
        return result;
    }
}
