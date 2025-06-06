package com.backend.bilibili.service.user.impl;

import com.backend.bilibili.mapper.user.UserInfoMapper;
import com.backend.bilibili.mapper.user.UserMapper;
import com.backend.bilibili.pojo.user.User;
import com.backend.bilibili.pojo.user.UserInfo;
import com.backend.bilibili.service.user.account.RegisterService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String,String> register(String username, String password, String confirmPassword){
        Map<String,String> map=new HashMap<>();
        if(username==null){
            map.put("error_message","用户名不能为空");
            return map;
        }
        if(password==null || confirmPassword==null){
            map.put("error_message","密码不能为空");
            return map;

        }
        username = username.trim();
        if(username.isEmpty()){
            map.put("error_message","用户名不能为空");
            return map;
        }

        if(password.isEmpty() || confirmPassword.isEmpty()){
            map.put("error_message","密码不能为空");
            return map;
        }

        if(username.length()>100){
            map.put("error_message","用户名太长");
            return map;
        }
        if(password.length()>100){
            map.put("error_message","密码太长");
            return map;
        }

        if(!password.equals(confirmPassword)){
            map.put("error_message","密码不一致");
            return map;
        }

        //查询数据库是不是有重名的username;
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        List<User> users = userMapper.selectList(queryWrapper);
        if (!users.isEmpty()){
            map.put("error_message","用户名已存在");
            return map;
        }

        //密码加密
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(null,username,encodedPassword);
        //数据库注入
        userMapper.insert(user);


        Long uid = user.getId();
        UserInfo userInfo = new UserInfo();
        userInfo.setUid(uid);
        userInfo.setUsername(username);
        userInfo.setRegisterTime(LocalDateTime.now());

        userInfoMapper.insert(userInfo);


        map.put("error_message","success");
        return map;

    }

}
