package com.backend.bilibili.service.user.account;

import com.backend.bilibili.pojo.user.UserFollow;

import java.util.List;

public interface UserFollowService {
    boolean follow(Long followerUid, Long followingUid);
    boolean unfollow(Long followerUid, Long followingUid);
    List<UserFollow> getFollowListByFollowerUid(Long followerUid);

    boolean  isFollow(Long followerUid, Long followingUid);
}
