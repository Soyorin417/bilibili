package com.backend.bilibili.mapper.recommend;

import com.backend.bilibili.pojo.recommend.UserTagScore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserTagScoreMapper extends BaseMapper<UserTagScore> {
    @Select("SELECT DISTINCT user_id FROM user_tag_score WHERE user_id != #{userId}")
    List<Long> getAllUserIdsExcept(Long userId);

}
