package com.backend.bilibili.service.video.comment;

import com.backend.bilibili.pojo.video.comment.ReplyInfo;
import com.backend.bilibili.service.dto.ReplyDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ReplyInfoService extends IService<ReplyInfo> {
    List<ReplyDTO> getRepliesByCommentId(Long commentId);

    boolean saveReply(ReplyDTO replyDTO);

    boolean updateReplyById(Long id, ReplyDTO replyDTO);

    boolean deleteReplyById(Long id);

    ReplyInfo getReplyById(Long id);


}
