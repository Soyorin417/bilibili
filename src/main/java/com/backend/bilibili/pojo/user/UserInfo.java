package com.backend.bilibili.pojo.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("userinfo")
public class UserInfo {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer uid;
    private String avatar;
    private String username;
    private int level;
    private int exp;
    private int coin;
    private int bcoin;
    private int following;
    private int fans;
    private int dynamic;
}
