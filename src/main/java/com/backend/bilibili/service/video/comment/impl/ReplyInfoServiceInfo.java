package com.backend.bilibili.service.video.comment.impl;

import com.backend.bilibili.mapper.video.comment.CommentInfoMapper;
import com.backend.bilibili.mapper.video.comment.ReplyInfoMapper;
import com.backend.bilibili.pojo.video.comment.ReplyInfo;
import com.backend.bilibili.service.vo.ReplyVO;
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
    public List<ReplyVO> getRepliesByCommentId(Long commentId) {
        return replyInfoMapper.selectRepliesWithUserInfo(commentId);
    }

    // 新增回复
    @Override
    public boolean saveReply(ReplyVO replyVO) {
        ReplyInfo replyInfo = new ReplyInfo();
        BeanUtils.copyProperties(replyVO, replyInfo);
        boolean result = save(replyInfo); // 只调用一次保存！

        if (result) {
            commentInfoMapper.increaseReplyCount(replyVO.getCommentId());
        }

        return result;
    }


    @Override
    public boolean updateReplyById(Long id, ReplyVO replyVO) {
        ReplyInfo replyInfo = new ReplyInfo();
        replyVO.setId(null);
        BeanUtils.copyProperties(replyVO, replyInfo);


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
