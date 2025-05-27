package com.backend.bilibili.service.user.account;

import com.backend.bilibili.pojo.user.UserFollow;

import java.util.List;

public interface UserFollowService {
    boolean follow(Long followerUid, Long followingUid, String remark);
    boolean unfollow(Long followerUid, Long followingUid);
    List<UserFollow> getFollowingList(Long followerUid);
}
