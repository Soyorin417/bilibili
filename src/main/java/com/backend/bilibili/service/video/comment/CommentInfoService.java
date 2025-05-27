package com.backend.bilibili.service.video.comment;

import com.backend.bilibili.service.dto.CommentDTO;
import com.backend.bilibili.pojo.video.comment.CommentInfo;
import com.backend.bilibili.service.vo.CommentVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface CommentInfoService extends IService<CommentInfo> {

    List<CommentVO> getCommentsWithUserInfo(Long videoId);

    void addComment(CommentDTO dto, Long userUid);

    boolean deleteCommentById(Long commentId);
}
