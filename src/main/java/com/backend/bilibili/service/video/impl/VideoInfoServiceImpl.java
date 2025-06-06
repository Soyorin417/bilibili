package com.backend.bilibili.service.video.impl;

import com.backend.bilibili.mapper.video.VideoInfoMapper;
import com.backend.bilibili.mapper.user.UserInfoMapper;
import com.backend.bilibili.mapper.video.danmaku.DanmakuMapper;
import com.backend.bilibili.pojo.user.UserInfo;
import com.backend.bilibili.pojo.video.Tag;
import com.backend.bilibili.pojo.video.VideoInfo;
import com.backend.bilibili.pojo.video.danmaku.Danmaku;
import com.backend.bilibili.service.dto.UploadVideoDTO;
import com.backend.bilibili.service.dto.VideoViewDTO;
import com.backend.bilibili.service.minio.MinioService;
import com.backend.bilibili.service.video.TagService;
import com.backend.bilibili.service.video.VideoInfoService;
import com.backend.bilibili.service.video.VideoTagService;
import com.backend.bilibili.service.video.danmaku.DanmakuService;
import com.backend.bilibili.utils.MinioUrlUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VideoInfoServiceImpl implements VideoInfoService {

    @Autowired
    private VideoInfoMapper videoInfoMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private DanmakuMapper danmakuMapper;
    @Autowired
    private MinioService minioService;

    @Autowired
    private DanmakuService danmakuService;

    @Autowired
    private TagService tagService;

    @Autowired
    private VideoTagService videoTagService;



    @Override
    public boolean increaseViewCount(Long videoId) {
        UpdateWrapper<VideoInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", videoId).setSql("views = views + 1");
        return videoInfoMapper.update(null, updateWrapper) > 0;
    }

    @Override
    public Long getVideoCount() {
        return videoInfoMapper.selectCount(null);
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

    public ResponseEntity<?> submitVideoTest(UploadVideoDTO dto) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 上传文件
            String videoUrl = minioService.putObject(dto.getVideoFile());
            String coverUrl = minioService.putObject(dto.getCoverFile());
            String danmakuUrl = minioService.putObject(dto.getDanmakuFileName());

            // 写入 video_info
            VideoInfo videoInfo = new VideoInfo();
            videoInfo.setTitle(dto.getTitle());
            videoInfo.setVideoUrl(videoUrl);
            videoInfo.setImage(coverUrl);
            videoInfo.setDescription(dto.getDescription());
            videoInfo.setDuration(dto.getDuration());
            videoInfo.setAuthorId(dto.getAuthorId());

            int rows = videoInfoMapper.insert(videoInfo);
            if (rows <= 0) {
                result.put("error_message", "视频信息入库失败");
                return ResponseEntity.badRequest().body(result);
            }

            int videoId = videoInfo.getId();

            // 写入 danmaku_list
            Danmaku danmaku = new Danmaku();
            danmaku.setVid(videoId);
            danmaku.setUrl(danmakuUrl);
            System.out.println(danmaku+"弹幕");
            danmakuService.addDanmaku(danmaku);

            // 写入 tag 和 video_tag
            List<String> tags = dto.getTags();
            if (dto.getTags() != null && !dto.getTags().isEmpty()) {
                for (String tagName : dto.getTags()) {
                    Tag tag = tagService.getTagByName(tagName);
                    if (tag == null) {
                        tag = new Tag();
                        tag.setName(tagName);
                        tag = tagService.createTag(tag); // 自动去重
                    }
                    videoTagService.addTagToVideo((long) videoId, tag.getId());
                }
            }

            result.put("error_message", "success");
            result.put("videoId", videoId);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            result.put("error_message", "上传失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }


    @Override
    public ResponseEntity<Map<String, Object>> submitVideoWithParams(
            String title,
            String videoFileName,
            String coverFileName,
            String description,
            String duration,
            Long authorId,
            String danmakuFileName) {

        VideoInfo videoInfo = new VideoInfo();
        videoInfo.setTitle(title);
        videoInfo.setVideoUrl(MinioUrlUtil.getUrl(videoFileName));
        videoInfo.setImage(MinioUrlUtil.getUrl(coverFileName));
        videoInfo.setDescription(description);
        videoInfo.setAuthorId(authorId);
        videoInfo.setDuration(formatDuration(duration));
        videoInfo.setTime(new Date());

        boolean success = submitVideo(videoInfo);

        if (success) {
            // 保存弹幕信息
            Danmaku danmaku = new Danmaku();
            danmaku.setVid(videoInfo.getId());
            danmaku.setUrl(MinioUrlUtil.getUrl(danmakuFileName));
            System.out.println(danmaku.getUrl());

            danmakuMapper.insert(danmaku);

            Map<String, Object> response = new HashMap<>();
            response.put("videoId", videoInfo.getId());
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> error = new HashMap<>();
            error.put("code", 500);
            error.put("message", "提交失败");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }



    @Override
    public List<VideoViewDTO> getAllVideoViewsByUserId(int userId) {
        QueryWrapper<VideoInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("author_id", userId);
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
            dto.setDuration(formatDuration(video.getDuration())); // 格式化时长
            dto.setLikeCount(video.getLikeCount());
            dto.setCollectCount(video.getCollectCount());
            dto.setCoinCount(video.getCoinCount());
            dto.setShareCount(video.getShareCount());
            dto.setStatus(video.getStatus());

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
    private String formatDuration(String durationStr) {
        try {
            double seconds = Double.parseDouble(durationStr);
            int totalSeconds = (int) Math.round(seconds);
            int minutes = totalSeconds / 60;
            int secondsPart = totalSeconds % 60;
            return String.format("%02d:%02d", minutes, secondsPart);
        } catch (Exception e) {
            return "00:00";
        }
    }


    @Override
    public boolean submitVideo(VideoInfo videoInfo) {
        try {
            int result = videoInfoMapper.insert(videoInfo);
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
