package com.backend.bilibili.mapper.message;

import com.backend.bilibili.pojo.message.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MessagesMapper extends BaseMapper<Message> {
    @Select("SELECT * FROM message WHERE session_id = #{sessionId} ORDER BY send_time ASC")
    List<Message> getMessagesBySessionId(@Param("sessionId") Long sessionId);
}
