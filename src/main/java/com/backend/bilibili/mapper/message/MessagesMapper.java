package com.backend.bilibili.mapper.message;

import com.backend.bilibili.pojo.message.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessagesMapper extends BaseMapper<Message> {
}
