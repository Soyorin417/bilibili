package com.backend.bilibili.service.user.impl;

import com.backend.bilibili.mapper.user.UserFollowMapper;
import com.backend.bilibili.mapper.user.UserInfoMapper;
import com.backend.bilibili.pojo.user.UserFollow;
import com.backend.bilibili.pojo.user.UserInfo;
import com.backend.bilibili.service.user.account.UserFollowService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserFollowServiceImpl implements UserFollowService {
    @Autowired
    private UserFollowMapper userFollowMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;


    public boolean follow(Long followerUid, Long followingUid, String remark) {
        QueryWrapper<UserFollow> query = new QueryWrapper<>();
        query.eq("follower_uid", followerUid).eq("following_uid", followingUid);
        UserFollow exist = userFollowMapper.selectOne(query);

        QueryWrapper<UserFollow> backQuery = new QueryWrapper<>();
        backQuery.eq("follower_uid", followingUid).eq("following_uid", followerUid).eq("status", 1);
        boolean isMutual = userFollowMapper.selectOne(backQuery) != null;

        boolean result;
        if (exist != null) {
            UpdateWrapper<UserFollow> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("follower_uid", followerUid).eq("following_uid", followingUid);

            UserFollow update = new UserFollow();
            update.setStatus(1);
            update.setRemark(remark);
            update.setIsMutual(isMutual ? 1 : 0);

            result = userFollowMapper.update(update, updateWrapper) > 0;
        } else {
            UserFollow follow = new UserFollow();
            follow.setFollowerUid(followerUid);
            follow.setFollowingUid(followingUid);
            follow.setStatus(1);
            follow.setIsMutual(isMutual ? 1 : 0);
            follow.setRemark(remark);

            result = userFollowMapper.insert(follow) > 0;
        }

        if (result) {
            // 粉丝数 +1
            UpdateWrapper<UserInfo> updateFans = new UpdateWrapper<>();
            updateFans.eq("uid", followingUid);

            updateFans.setSql("fans = fans + 1");
            userInfoMapper.update(null, updateFans);
        }


        return result;
    }



    public boolean unfollow(Long followerUid, Long followingUid) {
        QueryWrapper<UserFollow> query = new QueryWrapper<>();
        query.eq("follower_uid", followerUid).eq("following_uid", followingUid);
        UserFollow exist = userFollowMapper.selectOne(query);

        if (exist != null && exist.getStatus() == 1) {
            exist.setStatus(0);
            boolean result = userFollowMapper.updateById(exist) > 0;
            if (result) {
                // 粉丝数 -1，fans > 0 才减
                UpdateWrapper<UserInfo> updateFans = new UpdateWrapper<>();
                updateFans.eq("uid", followingUid).gt("fans", 0);
                updateFans.setSql("fans = fans - 1");
                userInfoMapper.update(null, updateFans);
            }
            return result;
        }
        return false;
    }


    public List<UserFollow> getFollowingList(Long followerUid) {
        QueryWrapper<UserFollow> query = new QueryWrapper<>();
        query.eq("follower_uid", followerUid).eq("status", 1);
        return userFollowMapper.selectList(query);
    }
}
