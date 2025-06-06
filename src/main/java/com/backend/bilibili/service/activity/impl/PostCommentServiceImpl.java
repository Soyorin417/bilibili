package com.backend.bilibili.service.activity.impl;

import com.backend.bilibili.mapper.activity.PostCommentMapper;
import com.backend.bilibili.mapper.user.UserInfoMapper;
import com.backend.bilibili.pojo.activity.PostComment;
import com.backend.bilibili.pojo.user.UserInfo;
import com.backend.bilibili.service.activity.PostCommentLikeService;
import com.backend.bilibili.service.activity.PostCommentService;
import com.backend.bilibili.service.dto.PostCommentDTO;
import com.backend.bilibili.service.user.account.InfoService;
import com.backend.bilibili.utils.UserTokenUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PostCommentServiceImpl implements PostCommentService {

    @Autowired
    private PostCommentMapper postCommentMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private PostCommentLikeService postCommentLikeService;

    @Autowired
    private InfoService infoService;

    @Override
    public void addComment(PostComment comment) {
        Long uid = UserTokenUtil.getUid();
        comment.setUserId(uid);
        comment.setCreateTime(LocalDateTime.now());
        comment.setUpdateTime(LocalDateTime.now());
        comment.setStatus(1); // 默认正常状态
        comment.setLikes(0);  // 默认点赞数为 0
        postCommentMapper.insert(comment);
    }

    @Override
    public List<PostCommentDTO> getCommentsByPostId(Long postId ,Long userId) {

        QueryWrapper<PostComment> query = new QueryWrapper<>();
        query.eq("post_id", postId)
                .isNull("parent_comment_id")
                .eq("status", 1)
                .orderByDesc("create_time");
        List<PostComment> comments = postCommentMapper.selectList(query);

        return comments.stream().map(comment -> {
            PostCommentDTO dto = new PostCommentDTO();
            BeanUtils.copyProperties(comment, dto);

            Map<String, Object> userInfo = infoService.getUserInfoById(comment.getUserId());
            if (userInfo != null) {
                dto.setUsername((String) userInfo.get("username"));
                dto.setAvatar((String) userInfo.get("avatar"));
            }


            dto.setLikeCount(postCommentLikeService.getLikeCount(comment.getId()));
            dto.setLiked(postCommentLikeService.isLiked(comment.getId()));

            // 查询回复
            List<PostComment> replies = getRepliesByParentId(comment.getId());
            List<PostCommentDTO> replyDTOs = replies.stream().map(reply -> {
                PostCommentDTO replyDto = new PostCommentDTO();
                BeanUtils.copyProperties(reply, replyDto);


                Map<String, Object> replyUser = infoService.getUserInfoById(comment.getUserId());
                if (replyUser != null) {
                    dto.setUsername((String) userInfo.get("username"));
                    dto.setAvatar((String) userInfo.get("avatar"));
                }

                replyDto.setLikeCount(postCommentLikeService.getLikeCount(reply.getId()));
                replyDto.setLiked(postCommentLikeService.isLiked(reply.getId()));
                return replyDto;
            }).collect(Collectors.toList());

            dto.setReplies(replyDTOs);

            return dto;
        }).collect(Collectors.toList());
    }




    @Override
    public List<PostComment> getRepliesByParentId(Long parentId) {
        return postCommentMapper.selectList(new QueryWrapper<PostComment>()
                .eq("parent_comment_id", parentId)
                .eq("status", 1)
                .orderByAsc("create_time"));
    }

    @Override
    public boolean deleteCommentById(Long id) {
        return postCommentMapper.deleteById(id) > 0;
    }

    private PostCommentDTO convertToDTO(PostComment comment) {
        PostCommentDTO dto = new PostCommentDTO();
        dto.setId(comment.getId());
        dto.setPostId(comment.getPostId());
        dto.setUserId(comment.getUserId());
        dto.setContent(comment.getContent());
        dto.setCreateTime(comment.getCreateTime());
        dto.setLikeCount(comment.getLikes());

        // 查询用户信息
        UserInfo user = userInfoMapper.selectById(comment.getUserId());
        if (user != null) {
            dto.setUsername(user.getUsername());
            dto.setAvatar(user.getAvatar());
        }

        // 查询子评论
        List<PostCommentDTO> replies = postCommentMapper.selectList(new QueryWrapper<PostComment>()
                        .eq("parent_comment_id", comment.getId())
                        .eq("status", 1)
                        .orderByAsc("create_time"))
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        dto.setReplies(replies);

        dto.setLiked(false); // 点赞逻辑如需支持再添加

        return dto;
    }
}
