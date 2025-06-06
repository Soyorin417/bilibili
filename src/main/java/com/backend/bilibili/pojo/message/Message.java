package com.backend.bilibili.pojo.message;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @TableId(type = IdType.AUTO)
    private Long id;

    @JsonProperty("session_id")
    private Long sessionId;

    private String content;

    @JsonProperty("send_time")
    private Date sendTime;

    @JsonProperty("sender_id")
    private Long senderId;

    @JsonProperty("sender_name")
    private String senderName;

    @JsonProperty("sender_avatar")
    private String senderAvatar;

    @JsonProperty("receiver_id")
    private Long receiverId;

    @JsonProperty("receiver_name")
    private String receiverName;

    @JsonProperty("receiver_avatar")
    private String receiverAvatar;

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", sessionId=" + sessionId +
                ", content='" + content + '\'' +
                ", sendTime=" + sendTime +
                ", senderId=" + senderId +
                ", senderName='" + senderName + '\'' +
                ", senderAvatar='" + senderAvatar + '\'' +
                ", receiverId=" + receiverId +
                ", receiverName='" + receiverName + '\'' +
                ", receiverAvatar='" + receiverAvatar + '\'' +
                '}';
    }
}
