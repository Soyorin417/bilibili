package com.backend.bilibili.service.video.impl;

import com.backend.bilibili.mapper.video.VideoInfoMapper;
import com.backend.bilibili.mapper.user.UserInfoMapper;
import com.backend.bilibili.pojo.user.UserInfo;
import com.backend.bilibili.pojo.video.VideoInfo;
import com.backend.bilibili.service.dto.VideoViewDTO;
import com.backend.bilibili.service.video.VideoInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VideoInfoServiceImpl implements VideoInfoService {

    @Autowired
    private VideoInfoMapper videoInfoMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public boolean increaseViewCount(Long videoId) {
        UpdateWrapper<VideoInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", videoId).setSql("views = views + 1");
        return videoInfoMapper.update(null, updateWrapper) > 0;
    }


    @Override
    public VideoViewDTO getVideoInfoById(int id) {
        try {
            QueryWrapper<VideoInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", id);
            VideoInfo videoInfo = videoInfoMapper.selectOne(queryWrapper);
            if (videoInfo == null) {
                throw new RuntimeException("id不存在");
            }

            VideoViewDTO dto = new VideoViewDTO();
            dto.setId(videoInfo.getId());
            dto.setTitle(videoInfo.getTitle());
            dto.setViews(videoInfo.getViews());
            dto.setComments(videoInfo.getComments());
            dto.setTime(videoInfo.getTime());
            dto.setDescription(videoInfo.getDescription());
            dto.setVideoUrl(videoInfo.getVideoUrl());
            dto.setImage(videoInfo.getImage());
            dto.setShowRight(videoInfo.isShowRight());
            dto.setAuthorId(videoInfo.getAuthorId());
            dto.setDuration(videoInfo.getDuration());
            dto.setLikeCount(videoInfo.getLikeCount());
            dto.setCollectCount(videoInfo.getCollectCount());
            dto.setCoinCount(videoInfo.getCoinCount());
            dto.setShareCount(videoInfo.getShareCount());
            dto.setStatus(videoInfo.getStatus());

            System.out.println(videoInfo.getAuthorId() + "authorId" + videoInfo.getAuthorId());

            if (videoInfo.getAuthorId() != null) {
                QueryWrapper<UserInfo> userQueryWrapper = new QueryWrapper<>();
                userQueryWrapper.eq("uid", videoInfo.getAuthorId());
                UserInfo author = userInfoMapper.selectOne(userQueryWrapper);
                if (author != null) {
                    dto.setAvatar(author.getAvatar());
                    dto.setFollow(String.valueOf(author.getFans())); // 粉丝数放这里
                    dto.setAuthorName(String.valueOf(author.getUsername()));
                }
            }

            return dto;

        } catch (Exception e) {
            System.err.println("数据库查询异常：" + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("数据库连接异常，请稍后再试");
        }
    }

    // 新增方法，返回带作者头像和粉丝数的 DTO 列表
    public List<VideoViewDTO> getAllVideoViews() {
        QueryWrapper<VideoInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "approved");
        List<VideoInfo> videoList = videoInfoMapper.selectList(queryWrapper);

        List<VideoViewDTO> dtoList = new ArrayList<>();

        for (VideoInfo video : videoList) {
            VideoViewDTO dto = new VideoViewDTO();
            dto.setId(video.getId());
            dto.setTitle(video.getTitle());
            dto.setViews(video.getViews());
            dto.setComments(video.getComments());
            dto.setTime(video.getTime());
            dto.setDescription(video.getDescription());
            dto.setVideoUrl(video.getVideoUrl());
            dto.setImage(video.getImage());
            dto.setShowRight(video.isShowRight());
            dto.setAuthorId(video.getAuthorId());
            dto.setDuration(video.getDuration());
            dto.setLikeCount(video.getLikeCount());
            dto.setCollectCount(video.getCollectCount());
            dto.setCoinCount(video.getCoinCount());
            dto.setShareCount(video.getShareCount());
            dto.setStatus(video.getStatus());


            // 查作者信息，填充头像和粉丝数
            if (video.getAuthorId() != null) {

                QueryWrapper<UserInfo> authorQueryWrapper = new QueryWrapper<>();
                authorQueryWrapper.eq("uid", video.getAuthorId());
                UserInfo author = userInfoMapper.selectOne(authorQueryWrapper);
                if (author != null) {
                    dto.setAvatar(author.getAvatar());
                    dto.setFollow(String.valueOf(author.getFans()));
                    dto.setAuthorName(String.valueOf(author.getUsername()));
                }
            }

            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public boolean submitVideo(VideoInfo videoInfo) {
        try {
            int result = videoInfoMapper.insert(videoInfo);
            System.out.println("insert result = " + result);
            System.out.println("inserted id = " + videoInfo.getId());
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteVideoById(int id) {
        return videoInfoMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateVideo(VideoInfo videoInfo) {
        return videoInfoMapper.updateById(videoInfo) > 0;
    }
}
