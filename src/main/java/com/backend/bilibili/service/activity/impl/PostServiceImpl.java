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
import com.backend.bilibili.utils.UserTokenUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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


    //查询某条动态的评论数
    private int getCommentCount(Long postId) {
        return Math.toIntExact(postCommentMapper.selectCount(
                new QueryWrapper<PostComment>().eq("post_id", postId)
        ));
    }

    //查询某条动态的点赞数
    private int getLikeCount(Long postId) {
        return Math.toIntExact(postLikeMapper.selectCount(
                new QueryWrapper<PostLike>().eq("post_id", postId)
        ));
    }

    //判断指定用户是否给该动态点赞
    private boolean isLikedByUser(Long postId, Long userId) {
        if (userId == null) return false;
        return postLikeMapper.selectCount(
                new QueryWrapper<PostLike>().eq("post_id", postId).eq("user_id", userId)
        ) > 0;
    }


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
        List<Post> posts = postMapper.selectList(new QueryWrapper<Post>().orderByDesc("create_time"));

        Set<Long> userIds = posts.stream().map(Post::getUserId).collect(Collectors.toSet());
        List<UserInfo> users = userInfoMapper.selectBatchIds(userIds);
        Map<Long, UserInfo> userMap = users.stream().collect(Collectors.toMap(UserInfo::getUid, u -> u));

        return posts.stream().map(post -> {
            PostDTO dto = new PostDTO();
            BeanUtils.copyProperties(post, dto);

            UserInfo user = userMap.get(post.getUserId());
            if (user != null) {
                dto.setUsername(user.getUsername());
                dto.setAvatar(user.getAvatar());
            }

            dto.setCommentCount(getCommentCount(post.getId()));
            dto.setLikeCount(getLikeCount(post.getId()));
            dto.setLiked(isLikedByUser(post.getId(), currentUserId));

            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public void addComment(PostComment comment) {
        Long uid = UserTokenUtil.getUid();
        comment.setUserId(uid);
        comment.setLikes(0);
        comment.setStatus(1);
        LocalDateTime now = LocalDateTime.now();
        comment.setCreateTime(now);
        comment.setUpdateTime(now);
        postCommentMapper.insert(comment);
    }

    @Override
    public boolean createPost(PostDTO postDTO) {
        Post post = new Post();
        BeanUtils.copyProperties(postDTO, post);
        int rows = postMapper.insert(post);
        return rows > 0;
    }


    @Override
    public void updatePost(PostDTO postDTO) {
        Post post = new Post();
        BeanUtils.copyProperties(postDTO, post);
        postMapper.updateById(post);
    }

    @Override
    public boolean deletePostById(Long id) {
        return postMapper.deleteById(id) > 0;
    }

    @Override
    public PostDTO getPostById(Long id, Long currentUserId) {
        Post post = postMapper.selectById(id);
        if (post == null) return null;

        PostDTO dto = new PostDTO();
        BeanUtils.copyProperties(post, dto);

        UserInfo user = userInfoMapper.selectById(post.getUserId());
        if (user != null) {
            dto.setUsername(user.getUsername());
            dto.setAvatar(user.getAvatar());
        }

        dto.setCommentCount(getCommentCount(post.getId()));
        dto.setLikeCount(getLikeCount(post.getId()));
        dto.setLiked(isLikedByUser(post.getId(), currentUserId));

        dto.setViews(post.getViews() != null ? post.getViews() : 0L);
        dto.setShareCount(post.getShares() != null ? post.getShares() : 0);
        dto.setShared(false);

        return dto;
    }
}

