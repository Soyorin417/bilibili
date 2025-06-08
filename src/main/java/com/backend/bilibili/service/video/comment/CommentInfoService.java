package com.backend.bilibili.service.video.comment;

import com.backend.bilibili.pojo.video.comment.CommentInfo;
import com.backend.bilibili.service.dto.CommentDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface CommentInfoService extends IService<CommentInfo> {

    List<CommentDTO> getCommentsWithUserInfo(Long videoId);

    void addComment(Long videoId, String content);

    boolean deleteCommentById(Long commentId);

    int getCommentCountByVideoId(Long videoId);
}
