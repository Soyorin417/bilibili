package com.backend.bilibili.service.user.impl;

import com.backend.bilibili.pojo.user.User;
import com.backend.bilibili.service.user.account.LoginService;
import com.backend.bilibili.service.user.utils.UserDetailsImpl;
import com.backend.bilibili.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Map<String, String> getToken(String username, String password) {
        // 创建认证token
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        try {
            // 尝试进行认证
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);

            // 调试信息：显示认证后的Authentication对象
            System.out.println("Authentication result: " + authenticate);

            if (authenticate.isAuthenticated()) {
                // 如果认证通过，获取用户信息
                UserDetailsImpl loginUser = (UserDetailsImpl) authenticate.getPrincipal();
                User user = loginUser.getUser();

                // 调试信息：显示用户信息
                System.out.println("Authentication successful for user: " + user.getUsername());

                // 生成JWT token
                String jwt = JwtUtil.createJWT(user.getUsername());

                // 调试信息：显示生成的JWT
                System.out.println("Generated JWT token: " + jwt);

                // 返回token
                Map<String, String> map = new HashMap<>();
                map.put("error_message", "success");
                map.put("token", jwt);

                return map;
            } else {
                // 如果认证失败，输出失败信息
                System.out.println("Authentication failed for username: " + username);
                Map<String, String> map = new HashMap<>();
                map.put("error_message", "Authentication failed");
                return map;
            }
        } catch (Exception e) {
            // 捕获异常并输出异常信息
            System.out.println("Authentication exception: " + e.getMessage());
            e.printStackTrace(); // 打印堆栈信息，方便调试

            // 异常返回
            Map<String, String> map = new HashMap<>();
            map.put("error_message", "Exception occurred: " + e.getMessage());
            return map;
        }
    }
}

