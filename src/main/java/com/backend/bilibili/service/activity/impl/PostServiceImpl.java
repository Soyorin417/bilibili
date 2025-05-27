package com.backend.bilibili.service.activity.impl;

import com.backend.bilibili.mapper.activity.PostCommentMapper;
import com.backend.bilibili.mapper.activity.PostLikeMapper;
import com.backend.bilibili.mapper.activity.PostMapper;
import com.backend.bilibili.mapper.user.UserInfoMapper;
import com.backend.bilibili.pojo.activity.Post;
import com.backend.bilibili.pojo.activity.PostComment;
import com.backend.bilibili.pojo.activity.PostLike;
import com.backend.bilibili.pojo.user.UserInfo;
import com.backend.bilibili.service.activity.PostService;
import com.backend.bilibili.service.dto.PostDTO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private PostCommentMapper postCommentMapper;

    @Autowired
    private PostLikeMapper postLikeMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;
    @Override
    public Post getPostById(Long id) {
        return postMapper.selectById(id);
    }

    @Override
    public List<Post> getPostsByUserId(Long userId) {
        QueryWrapper<Post> query = new QueryWrapper<>();
        query.eq("user_id", userId);
        return postMapper.selectList(query);
    }

    @Override
    public List<Post> searchPostsByContent(String keyword) {
        QueryWrapper<Post> query = new QueryWrapper<>();
        query.like("content", keyword);
        return postMapper.selectList(query);
    }

    @Override
    public List<PostDTO> getAllPostDTOs(Long currentUserId) {
        // 1. 查出所有动态
        List<Post> posts = postMapper.selectList(new QueryWrapper<Post>().orderByDesc("create_time"));

        // 2. 获取所有动态的 userId
        Set<Long> userIds = posts.stream().map(Post::getUserId).collect(Collectors.toSet());

        // 3. 查用户信息（头像、昵称）
        List<UserInfo> users = userInfoMapper.selectBatchIds(userIds);
        Map<Long, UserInfo> userMap = users.stream().collect(Collectors.toMap(UserInfo::getUid, u -> u));

        // 4. 查每条动态的评论数、点赞数、是否被当前用户点赞
        List<PostDTO> dtoList = new ArrayList<>();
        for (Post post : posts) {
            PostDTO dto = new PostDTO();
            BeanUtils.copyProperties(post, dto);

            // 用户信息
            UserInfo user = userMap.get(post.getUserId());
            if (user != null) {
                dto.setUsername(user.getUsername());
                dto.setAvatar(user.getAvatar());
            }

            // 评论数
            int commentCount = Math.toIntExact(postCommentMapper.selectCount(
                    new QueryWrapper<PostComment>().eq("post_id", post.getId())
            ));
            dto.setCommentCount(commentCount);

            // 点赞数
            int likeCount = Math.toIntExact(postLikeMapper.selectCount(
                    new QueryWrapper<PostLike>().eq("post_id", post.getId())
            ));
            dto.setLikeCount(likeCount);

            // 是否被当前用户点赞（可选）
            boolean liked = postLikeMapper.selectCount(
                    new QueryWrapper<PostLike>().eq("post_id", post.getId()).eq("user_id", currentUserId)
            ) > 0;
            dto.setLiked(liked);

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public void createPost(PostDTO postDTO) {
        Post post = new Post();
        BeanUtils.copyProperties(postDTO, post);
        postMapper.insert(post);
    }

    @Override
    public void updatePost(PostDTO postDTO) {
        Post post = new Post();
        BeanUtils.copyProperties(postDTO, post);
        postMapper.updateById(post);
    }

    @Override
    public void deletePostById(Long id) {
        postMapper.deleteById(id);
    }

    @Override
    public PostDTO getPostById(Long id, Long currentUserId) {
        Post post = postMapper.selectById(id);
        if (post == null) return null;

        PostDTO dto = new PostDTO();
        BeanUtils.copyProperties(post, dto);

        // 用户信息
        UserInfo user = userInfoMapper.selectById(post.getUserId());
        if (user != null) {
            dto.setUsername(user.getUsername());
            dto.setAvatar(user.getAvatar());
        }

        // 评论数
        int commentCount = Math.toIntExact(postCommentMapper.selectCount(
                new QueryWrapper<PostComment>().eq("post_id", post.getId())
        ));
        dto.setCommentCount(commentCount);

        // 点赞数
        int likeCount = Math.toIntExact(postLikeMapper.selectCount(
                new QueryWrapper<PostLike>().eq("post_id", post.getId())
        ));
        dto.setLikeCount(likeCount);

        // 当前用户是否点赞
        boolean liked = postLikeMapper.selectCount(
                new QueryWrapper<PostLike>().eq("post_id", post.getId()).eq("user_id", currentUserId)
        ) > 0;
        dto.setLiked(liked);

        // 浏览量（若字段存在）
        dto.setViews(post.getViews() != null ? post.getViews() : 0L);

        // 分享数
        dto.setShareCount(post.getShares() != null ? post.getShares() : 0);

        // 当前用户是否转发（如果你后面实现了分享记录表，可以补充逻辑）
        dto.setShared(false); // 先默认 false

        return dto;
    }



}
