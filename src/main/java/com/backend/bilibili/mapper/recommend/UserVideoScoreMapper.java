package com.backend.bilibili.mapper.recommend;

import com.backend.bilibili.pojo.recommend.UserVideoScore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface UserVideoScoreMapper extends BaseMapper<UserVideoScore> {

    @Select("SELECT DISTINCT user_id FROM user_video_score WHERE user_id != #{userId}")
    List<Long> getAllUserIdsExcept(@Param("userId") Long userId);
}
