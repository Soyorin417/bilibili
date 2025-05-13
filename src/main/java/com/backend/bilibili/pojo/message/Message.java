package com.backend.bilibili.pojo.message;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("message")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long sessionId;
    private String content;
    private Date sendTime;
    private Long senderId;
    private String avatar;
    private String name;


    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", sessionId=" + sessionId +
                ", content='" + content + '\'' +
                ", sendTime=" + sendTime +
                ", senderId=" + senderId +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}

