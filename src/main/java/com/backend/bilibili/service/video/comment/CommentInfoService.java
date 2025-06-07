package com.backend.bilibili.service.video.comment;

import com.backend.bilibili.pojo.video.comment.CommentInfo;
import com.backend.bilibili.service.dto.DanmuDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface CommentInfoService extends IService<CommentInfo> {

    List<DanmuDTO.CommentDTO> getCommentsWithUserInfo(Long videoId);

    void addComment(com.backend.bilibili.service.dto.CommentDTO dto, Long userUid);

    boolean deleteCommentById(Long commentId);

    int getCommentCountByVideoId(Long videoId);
}
