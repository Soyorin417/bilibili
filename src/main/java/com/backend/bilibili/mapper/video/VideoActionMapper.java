package com.backend.bilibili.mapper.video;

import com.backend.bilibili.pojo.video.VideoActionInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

@Mapper
public interface VideoActionMapper extends BaseMapper<VideoActionInfo> {



    @Select("SELECT * FROM video_action_info WHERE user_uid = #{userUid} AND video_id = #{videoId}")
    VideoActionInfo selectByUserAndVideo(@Param("userUid") Long userUid, @Param("videoId") Long videoId);
}
