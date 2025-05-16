package com.backend.bilibili.service.video;

import com.backend.bilibili.pojo.video.VideoActionInfo;

public interface VideoActionService {
    VideoActionInfo getUserVideoAction(Long videoId, Long userUid);

    boolean likeVideo(Long videoId, Long userUid);


    boolean cancelLike(Long videoId, Long userUid);

    boolean collectVideo(Long videoId, Long userUid);
    boolean cancelCollect(Long videoId, Long userUid);

    boolean coinVideo(Long videoId, Long userUid);
    boolean cancelCoin(Long videoId, Long userUid);

    boolean shareVideo(Long videoId, Long userUid);
}
