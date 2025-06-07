package com.backend.bilibili.service.video.comment.impl;

import com.backend.bilibili.mapper.video.comment.CommentInfoMapper;
import com.backend.bilibili.mapper.video.comment.ReplyInfoMapper;
import com.backend.bilibili.pojo.video.comment.ReplyInfo;
import com.backend.bilibili.service.dto.ReplyDTO;
import com.backend.bilibili.service.video.comment.ReplyInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplyInfoServiceInfo extends ServiceImpl<ReplyInfoMapper, ReplyInfo> implements ReplyInfoService {

    @Autowired
    ReplyInfoMapper replyInfoMapper;

    @Autowired
    CommentInfoMapper commentInfoMapper;

    // 查询评论的所有回复
    @Override
    public List<ReplyDTO> getRepliesByCommentId(Long commentId) {
        return replyInfoMapper.selectRepliesWithUserInfo(commentId);
    }

    // 新增回复
    @Override
    public boolean saveReply(ReplyDTO replyDTO) {
        ReplyInfo replyInfo = new ReplyInfo();
        BeanUtils.copyProperties(replyDTO, replyInfo);
        boolean result = save(replyInfo); // 只调用一次保存！

        if (result) {
            commentInfoMapper.increaseReplyCount(replyDTO.getCommentId());
        }

        return result;
    }


    @Override
    public boolean updateReplyById(Long id, ReplyDTO replyDTO) {
        ReplyInfo replyInfo = new ReplyInfo();
        replyDTO.setId(null);
        BeanUtils.copyProperties(replyDTO, replyInfo);


        ReplyInfo existing = replyInfoMapper.selectById(id);
        if (existing == null) {
            System.out.println("数据库中没有找到id = " + id + " 的回复");
            return false;
        }
        return this.updateById(replyInfo);
    }




    @Override
    public boolean deleteReplyById(Long id) {
        ReplyInfo replyInfo = replyInfoMapper.selectById(id);
        if (replyInfo == null) return false;

        boolean result = removeById(id);
        if (result) {
            commentInfoMapper.decreaseReplyCount(replyInfo.getCommentId());
        }
        return result;
    }



    // 查询单个回复
    @Override
    public ReplyInfo getReplyById(Long id) {
        return getById(id);
    }


}
