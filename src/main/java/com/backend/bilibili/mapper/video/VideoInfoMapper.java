package com.backend.bilibili.mapper.video;

import com.backend.bilibili.pojo.video.VideoInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface VideoInfoMapper extends BaseMapper<VideoInfo> {
    @Update("UPDATE video_info SET like_count = like_count + 1 WHERE id = #{videoId}")
    void increaseLikeCount(@Param("videoId") Long videoId);

    @Update("UPDATE video_info SET like_count = like_count - 1 WHERE id = #{videoId} AND like_count > 0")
    void decreaseLikeCount(@Param("videoId") Long videoId);

    @Update("UPDATE video_info SET collect_count = collect_count + 1 WHERE id = #{videoId}")
    void increaseCollectCount(@Param("videoId") Long videoId);

    @Update("UPDATE video_info SET collect_count = collect_count - 1 WHERE id = #{videoId} AND collect_count > 0")
    void decreaseCollectCount(@Param("videoId") Long videoId);

    @Update("UPDATE video_info SET coin_count = coin_count + 1 WHERE id = #{videoId}")
    void increaseCoinCount(@Param("videoId") Long videoId);

    @Update("UPDATE video_info SET coin_count = coin_count - 1 WHERE id = #{videoId} AND coin_count > 0")
    void decreaseCoinCount(@Param("videoId") Long videoId);

    @Update("UPDATE video_info SET share_count = share_count + 1 WHERE id = #{videoId}")
    void increaseShareCount(@Param("videoId") Long videoId);
}
