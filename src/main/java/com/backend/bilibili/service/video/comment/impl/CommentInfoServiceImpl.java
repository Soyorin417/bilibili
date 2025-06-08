package com.backend.bilibili.service.video.comment.impl;

import com.backend.bilibili.mapper.user.UserInfoMapper;
import com.backend.bilibili.mapper.video.comment.CommentInfoMapper;
import com.backend.bilibili.pojo.user.UserInfo;
import com.backend.bilibili.pojo.video.comment.CommentInfo;
import com.backend.bilibili.service.dto.CommentDTO;
import com.backend.bilibili.pojo.video.comment.ReplyInfo;
import com.backend.bilibili.service.user.account.InfoService;
import com.backend.bilibili.service.video.comment.CommentInfoService;
import com.backend.bilibili.service.video.comment.ReplyInfoService;
import com.backend.bilibili.utils.UserTokenUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CommentInfoServiceImpl extends ServiceImpl<CommentInfoMapper, CommentInfo> implements CommentInfoService {
    @Autowired
    CommentInfoMapper commentInfoMapper;

    @Autowired
    ReplyInfoService replyInfoService;

    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    public List<CommentDTO> getCommentsWithUserInfo(Long videoId) {
        // 1. 查询该视频所有评论
        QueryWrapper<CommentInfo> commentWrapper = new QueryWrapper<>();
        commentWrapper.eq("video_id", videoId);
        List<CommentInfo> commentInfos = commentInfoMapper.selectList(commentWrapper);

        if (commentInfos.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. 收集所有评论的 userUid 去重
        Set<Long> userUids = commentInfos.stream()
                .map(CommentInfo::getUserUid)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (userUids.isEmpty()) {
            // 没有用户id，直接返回空列表或只返回评论数据（看需求）
            return Collections.emptyList();
        }

        // 3. 批量查询用户信息
        QueryWrapper<UserInfo> userWrapper = new QueryWrapper<>();
        userWrapper.in("uid", userUids);
        List<UserInfo> userInfos = userInfoMapper.selectList(userWrapper);

        // 4. 把用户列表转成 Map，方便后续查找
        Map<Long, UserInfo> userMap = userInfos.stream()
                .collect(Collectors.toMap(UserInfo::getUid, Function.identity()));

        // 5. 组装 CommentDTO
        List<CommentDTO> commentDTOList = commentInfos.stream().map(comment -> {
            CommentDTO dto = new CommentDTO();
            // 复制评论字段
            BeanUtils.copyProperties(comment, dto);

            // 补充用户信息
            UserInfo user = userMap.get(comment.getUserUid());
            if (user != null) {
                dto.setUsername(user.getUsername());
                dto.setAvatar(user.getAvatar());
                dto.setLevel(user.getLevel());
            }

            return dto;
        }).collect(Collectors.toList());

        return commentDTOList;
    }


    @Override
    public void addComment(Long videoId, String content) {
        Long userUid = UserTokenUtil.getUid();
        if (userUid == null) {
            throw new RuntimeException("请先登录");
        }

        CommentInfo comment = new CommentInfo();
        comment.setVideoId(videoId);
        comment.setUserUid(userUid);
        comment.setContent(content);
        comment.setCreateTime(LocalDateTime.now());

        this.save(comment);
    }



    @Override
    public boolean deleteCommentById(Long commentId) {
        System.out.println("开始删除评论，commentId = " + commentId);

        // 删除回复前先查询
        System.out.println("开始删除该评论下的回复");
        QueryWrapper<ReplyInfo> replyWrapper = new QueryWrapper<>();
        replyWrapper.eq("comment_id", commentId);
        boolean removeRepliesResult = replyInfoService.remove(replyWrapper);
        System.out.println("删除回复结果: " + removeRepliesResult);

        System.out.println("开始删除评论");
        boolean removeCommentResult = this.removeById(commentId);
        System.out.println("删除评论结果: " + removeCommentResult);

        return removeCommentResult;
    }

    @Override
    public int getCommentCountByVideoId(Long videoId) {

        List<CommentInfo> comments = commentInfoMapper.selectList(
                new QueryWrapper<CommentInfo>().eq("video_id", videoId)
        );

        int commentCount = comments.size(); // 一级评论数量
        int totalReplyCount = comments.stream()
                .mapToInt(comment -> comment.getReplyCount() == null ? 0 : comment.getReplyCount())
                .sum();

        return commentCount + totalReplyCount;
    }



}
