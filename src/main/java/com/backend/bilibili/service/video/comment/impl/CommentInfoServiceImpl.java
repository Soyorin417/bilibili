package com.backend.bilibili.service.video.comment.impl;

import com.backend.bilibili.mapper.video.comment.CommentInfoMapper;
import com.backend.bilibili.service.dto.CommentDTO;
import com.backend.bilibili.pojo.video.comment.CommentInfo;
import com.backend.bilibili.service.vo.CommentVO;
import com.backend.bilibili.pojo.video.comment.ReplyInfo;
import com.backend.bilibili.service.video.comment.CommentInfoService;
import com.backend.bilibili.service.video.comment.ReplyInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.util.List;

@Service
public class CommentInfoServiceImpl extends ServiceImpl<CommentInfoMapper, CommentInfo> implements CommentInfoService {
    @Autowired
    CommentInfoMapper commentInfoMapper;

    @Autowired
    ReplyInfoService replyInfoService;

    @Override
    public List<CommentVO> getCommentsWithUserInfo(Long videoId) {
        return commentInfoMapper.selectCommentsWithUserInfo(videoId);
    }

    @Override
    public void addComment(CommentDTO dto, Long userUid) {
        CommentInfo comment = new CommentInfo();
        comment.setVideoId(dto.getVideoId());
        comment.setUserUid(userUid);
        comment.setContent(dto.getContent());

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



}
