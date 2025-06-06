package com.backend.bilibili.controller.admin.user;

import com.backend.bilibili.service.user.account.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class UserManageController {
    @Autowired
    InfoService infoService;
    @GetMapping("/user/getAllUser")
    public List<Map<String, Object>> getAllUserInfo(){
        return infoService.getAllUserInfo();
    }

    @PostMapping("/ban")
    public ResponseEntity<?> banUser(@RequestBody Map<String, Long> payload) {
        Long uid = payload.get("uid");
        System.out.println(uid + " uid");
        boolean result = infoService.banUser(uid);
        return result ? ResponseEntity.ok("封号成功") : ResponseEntity.badRequest().body("封号失败");
    }

    @PostMapping("/unban")
    public ResponseEntity<?> unbanUser(@RequestBody Map<String, Long> payload) {
        Long uid = payload.get("uid");
        boolean result = infoService.unbanUser(uid);
        return result ? ResponseEntity.ok("解封成功") : ResponseEntity.badRequest().body("解封失败");
    }

    @GetMapping("/user/getUserCount")
    public Long getUserCount(){
        return infoService.getAllUserInfoCount();
    }

    @GetMapping("/user/getByUsername")
    public ResponseEntity<?> getUserByUsername(@RequestParam String username) {
        if (username == null || username.isEmpty()) {
            return ResponseEntity.badRequest().body("用户名不能为空");
        }
        Map<String, Object> userInfo = infoService.getUserInfoByUserName(username);
        if (userInfo == null || userInfo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userInfo);
    }
    @GetMapping("/user/getById")
    public ResponseEntity<?> getUserById(@RequestParam Long uid) {
        if (uid == null) {
            return ResponseEntity.badRequest().body("用户ID不能为空");
        }
        Map<String, Object> userInfo = infoService.getUserInfoById(uid);
        if (userInfo == null || userInfo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userInfo);
    }



}
