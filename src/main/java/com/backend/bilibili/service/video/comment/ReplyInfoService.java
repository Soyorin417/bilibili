package com.backend.bilibili.service.video.comment;

import com.backend.bilibili.pojo.video.comment.ReplyInfo;
import com.backend.bilibili.pojo.video.comment.ReplyVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ReplyInfoService extends IService<ReplyInfo> {
    List<ReplyVO> getRepliesByCommentId(Long commentId);

    boolean saveReply(ReplyVO replyVO);

    boolean updateReplyById(Long id, ReplyVO replyVO);

    boolean deleteReplyById(Long id);

    ReplyInfo getReplyById(Long id);


}
