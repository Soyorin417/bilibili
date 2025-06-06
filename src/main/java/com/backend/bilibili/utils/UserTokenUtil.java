package com.backend.bilibili.utils;

import com.backend.bilibili.mapper.user.UserInfoMapper;
import com.backend.bilibili.pojo.user.UserInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

@Component
public class UserTokenUtil {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserInfoMapper userInfoMapper;

    private static UserTokenUtil instance;

    @PostConstruct
    public void init() {
        instance = this;
        instance.request = this.request;
        instance.userInfoMapper = this.userInfoMapper;
    }

    // 从请求中获取Token
    private String getToken() {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

    // 获取用户名
    public static String getUsername() {
        String token = instance.getToken();
        if (token == null) return null;
        try {
            return JwtUtil.getUsername(token);
        } catch (Exception e) {
            throw new RuntimeException("Token 解析失败：" + e.getMessage());
        }
    }

    // 获取当前用户对象
    public static UserInfo getCurrentUser() {
        String username = getUsername();
        if (username == null) return null;
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return instance.userInfoMapper.selectOne(queryWrapper);
    }

    // 获取当前用户id
    public static Long getUid() {
        UserInfo user = getCurrentUser();
        return user != null ? user.getUid() : null;
    }
}

