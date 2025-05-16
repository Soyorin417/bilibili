package com.backend.bilibili.pojo.video.comment;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentLike {
    private Long id;
    private Long commentId;
    private Long userUid;
}
