package com.backend.bilibili.service.user.impl;

import com.backend.bilibili.mapper.user.UserInfoMapper;
import com.backend.bilibili.pojo.user.User;
import com.backend.bilibili.pojo.user.UserInfo;
import com.backend.bilibili.service.user.account.InfoService;
import com.backend.bilibili.service.user.utils.UserDetailsImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class InfoServiceImpl implements InfoService {

    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    public Map<String, Object> getinfo() {
        QueryWrapper<UserInfo> queryWrapper = getUserInfoQueryWrapper();
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);
        if (userInfo == null) {
            throw new RuntimeException("用户不存在");
        }

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("error_message", "success");
        // 将用户信息直接放入顶层
        responseMap.put("id", userInfo.getUid());
        responseMap.put("username", userInfo.getUsername());
        responseMap.put("avatar", userInfo.getAvatar());
        responseMap.put("level", userInfo.getLevel());
        responseMap.put("exp", userInfo.getExp());
        responseMap.put("coin", userInfo.getCoin());
        responseMap.put("bcoin", userInfo.getBcoin());
        responseMap.put("fans", userInfo.getFans());
        responseMap.put("dynamic", userInfo.getDynamic());
        responseMap.put("role", userInfo.getRole());

        return responseMap;
    }

    private static QueryWrapper<UserInfo> getUserInfoQueryWrapper() {
        // 获取认证信息
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        if (authenticationToken == null || authenticationToken.getPrincipal() == null) {
            throw new RuntimeException("用户未认证或认证信息丢失");
        }

        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        String username = user.getUsername();

        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return queryWrapper;
    }

    public Map<String, Object> decreaseCoin(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("减少金额必须大于0");
        }

        QueryWrapper<UserInfo> queryWrapper = getUserInfoQueryWrapper();
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);
        if (userInfo == null) {
            throw new RuntimeException("用户不存在");
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