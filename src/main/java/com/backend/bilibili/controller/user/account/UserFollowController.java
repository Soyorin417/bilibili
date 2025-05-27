package com.backend.bilibili.controller.user.account;

import com.backend.bilibili.pojo.user.UserFollow;
import com.backend.bilibili.service.user.account.UserFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follow")
public class UserFollowController {
    @Autowired
    private UserFollowService userFollowService;

    @PostMapping("/follow")
    public String follow(@RequestParam Long followerUid, @RequestParam Long followingUid, @RequestParam(required = false) String remark) {
        boolean result = userFollowService.follow(followerUid, followingUid, remark);
        return result ? "关注成功" : "关注失败";
    }

    @PostMapping("/unfollow")
    public String unfollow(@RequestParam Long followerUid, @RequestParam Long followingUid) {
        boolean result = userFollowService.unfollow(followerUid, followingUid);
        return result ? "取消关注成功" : "取消关注失败";
    }

    @GetMapping("/list")
    public List<UserFollow> getFollowingList(@RequestParam Long followerUid) {
        return userFollowService.getFollowingList(followerUid);
    }
}

