package com.backend.bilibili.pojo.message;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("session")
public class Session implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private String avatar;
    private String name;
    private String summary;
    private Long user1Id;
    private Long user2Id;



    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", avatar='" + avatar + '\'' +
                ", name='" + name + '\'' +
                ", summary='" + summary + '\'' +
                ", user1Id=" + user1Id +
                ", user2Id=" + user2Id +
                '}';
    }
}

