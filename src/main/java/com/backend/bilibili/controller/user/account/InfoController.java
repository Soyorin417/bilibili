package com.backend.bilibili.controller.user.account;

import com.backend.bilibili.service.user.account.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class InfoController {

    @Autowired
    private InfoService infoService;

    // 获取当前登录用户信息
    @GetMapping("/info")
    public Map<String, Object> getCurrentUserInfo() {
        return infoService.getCurrentUserInfo();
    }

    // 根据 uid 获取指定用户信息
    @GetMapping("/{uid}")
    public Map<String, Object> getUserByUid(@PathVariable Long uid) {
        return infoService.getUserByUid(uid);
    }

    // 修改用户信息
    @PutMapping("/update")
    public Map<String, Object> updateUser(@RequestBody Map<String, Object> userInfo) {
        return infoService.updateUser(userInfo);
    }

    // 删除用户信息
    @DeleteMapping("/{uid}")
    public Map<String, Object> deleteUser(@PathVariable Long uid) {
        return infoService.deleteUser(uid);
    }

    @PostMapping("/update/avatar")
    public Map<String, Object> updateUserInfo(
            @RequestPart(value = "avatar", required = false) MultipartFile avatar,
            @RequestPart Map<String, Object> updateInfo) {
        return infoService.updateUserInfoWithAvatar(avatar, updateInfo);
    }
}
