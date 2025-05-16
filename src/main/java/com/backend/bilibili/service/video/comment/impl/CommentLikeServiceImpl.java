package com.backend.bilibili.service.video.comment.impl;

import com.backend.bilibili.mapper.video.comment.CommentInfoMapper;
import com.backend.bilibili.mapper.video.comment.CommentLikeMapper;
import com.backend.bilibili.pojo.video.comment.CommentInfo;
import com.backend.bilibili.pojo.video.comment.CommentLike;
import com.backend.bilibili.service.video.comment.CommentLikeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

@Service
public class CommentLikeServiceImpl extends ServiceImpl<CommentLikeMapper, CommentLike> implements CommentLikeService {

    @Autowired
    private CommentLikeMapper commentLikeMapper;

    @Autowired
    private CommentInfoMapper commentInfoMapper;

    @Override
    public boolean addLike(Long commentId, Long userUid) {
        // 先判断是否已点赞，防止重复点赞
        QueryWrapper<CommentLike> query = new QueryWrapper<>();
        query.eq("comment_id", commentId).eq("user_uid", userUid);
        Long count = commentLikeMapper.selectCount(query);

        if (count != null && count > 0) {
            return false;  // 已点赞，不重复添加
        }

        CommentLike like = new CommentLike();
        like.setCommentId(commentId);
        like.setUserUid(userUid);

        CommentInfo update = new CommentInfo();
        update.setId(commentId);
        update.setIsLiked(true);
        commentInfoMapper.updateById(update);

        boolean inserted = commentLikeMapper.insert(like) > 0;
        if (inserted) {
            // 点赞成功后，更新 comment_info 表的 like_count
            commentInfoMapper.increaseLikeCount(commentId);
        }
        return inserted;
    }

    @Override
    public boolean removeLike(Long commentId, Long userUid) {
        QueryWrapper<CommentLike> query = new QueryWrapper<>();
        query.eq("comment_id", commentId).eq("user_uid", userUid);

        boolean deleted = commentLikeMapper.delete(query) > 0;
        if (deleted) {
            commentInfoMapper.decreaseLikeCount(commentId);
        }

        CommentInfo update = new CommentInfo();
        update.setId(commentId);
        update.setIsLiked(false);
        commentInfoMapper.updateById(update);
        return deleted;
    }


    @Override
    public boolean isLiked(Long commentId, Long userUid) {
        QueryWrapper<CommentLike> query = new QueryWrapper<>();
        query.eq("comment_id", commentId).eq("user_uid", userUid);
        Long count = commentLikeMapper.selectCount(query);
        return count != null && count > 0;
    }
}
