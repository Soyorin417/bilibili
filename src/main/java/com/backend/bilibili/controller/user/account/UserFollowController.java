package com.backend.bilibili.controller.user.account;

import com.backend.bilibili.pojo.user.UserFollow;
import com.backend.bilibili.service.user.account.UserFollowService;
import com.backend.bilibili.utils.UserTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/follow")
public class UserFollowController {

    @Autowired
    private UserFollowService userFollowService;

    // 关注用户
    @PostMapping("/follow")
    public String follow(@RequestParam Long followingUid,
                         @RequestParam(required = false) String remark) {

        Long followerUid = UserTokenUtil.getUid();

        if (Objects.equals(followerUid, followingUid)) {
            return "不能关注自己";
        }

        boolean result = userFollowService.follow(followerUid, followingUid);
        return result ? "关注成功" : "关注失败";
    }

    // 取消关注
    @PostMapping("/unfollow")
    public String unfollow(@RequestParam Long followingUid) {
        Long followerUid = UserTokenUtil.getUid();
        boolean result = userFollowService.unfollow(followerUid, followingUid);
        return result ? "取消关注成功" : "取消关注失败";
    }

    // 获取当前用户关注列表
    @GetMapping("/list")
    public List<UserFollow> getFollowList() {
        Long followerUid = UserTokenUtil.getUid();
        return userFollowService.getFollowListByFollowerUid(followerUid);
    }
}
