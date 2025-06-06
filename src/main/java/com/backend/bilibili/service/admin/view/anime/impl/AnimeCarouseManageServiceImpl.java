package com.backend.bilibili.service.admin.view.anime.impl;

import com.backend.bilibili.mapper.anime.AnimeCarouselMapper;
import com.backend.bilibili.pojo.anime.AnimeCarousel;
import com.backend.bilibili.service.admin.view.anime.AnimeCarouseManageService;
import com.backend.bilibili.service.minio.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnimeCarouseManageServiceImpl implements AnimeCarouseManageService {
    @Autowired
    private AnimeCarouselMapper animeCarouselMapper;
    @Autowired
    private MinioService minioService;


    //获取全部轮播图
    @Override
    public List<AnimeCarousel> getAllCarousel() {
        return animeCarouselMapper.selectList(null);
    }

    //添加新的轮播图
    @Override
    public Map<String, Object> addCarouselWithImage(MultipartFile imageFile,String fileName) {
        Map<String, Object> result = new HashMap<>();

        if (imageFile == null || imageFile.isEmpty()) {
            result.put("error_message", "轮播图图片不能为空");
            return result;
        }

        try {
            String imageUrl = minioService.putObject(imageFile);

            AnimeCarousel carousel = new AnimeCarousel();
            carousel.setUrl(minioService.putObject(imageFile));

            //插入数据库
            int rows = animeCarouselMapper.insert(carousel);
            if (rows <= 0) {
                result.put("error_message", "插入失败");
                return result;
            }

            result.put("error_message", "success");
            result.put("imageUrl", imageUrl);
            return result;

        } catch (Exception e) {
            result.put("error_message", "图片上传失败：" + e.getMessage());
            return result;
        }
    }


    //更新轮播图信息
    @Override
    public Map<String, Object> updateCarouselWithImage(Long id, MultipartFile imageFile) {
        Map<String, Object> result = new HashMap<>();

        // 1. 查找原始记录
        AnimeCarousel carousel = animeCarouselMapper.selectById(id);
        if (carousel == null) {
            result.put("error_message", "轮播图不存在");
            return result;
        }

        try {
            // 2. 有新图片则删除旧图，上传新图，更新URL
            if (imageFile != null && !imageFile.isEmpty()) {
                String oldFileName = carousel.getUrl(); // 旧文件名

                // 删除旧图片（旧文件名不为空再删）
                if (oldFileName != null && !oldFileName.isEmpty()) {
                    minioService.removeObject("image", oldFileName);
                }

                // 上传新图片
                String newFileName = minioService.putObject(imageFile);
                carousel.setUrl(newFileName);
                result.put("url", newFileName);
            }

            // 3. 更新数据库记录
            int rows = animeCarouselMapper.updateById(carousel);
            if (rows <= 0) {
                result.put("error_message", "更新失败");
                return result;
            }

            result.put("error_message", "success");
            return result;

        } catch (Exception e) {
            result.put("error_message", "更新失败：" + e.getMessage());
            return result;
        }
    }



    //删除轮播图
    @Override
    public boolean deleteCarouselById(Long id) throws Exception {
        AnimeCarousel carousel = animeCarouselMapper.selectById(id);
        if (carousel == null) return false;

        // 删除 MinIO 中的文件
        String fileName = carousel.getUrl(); // 注意：fileName 是 MinIO 的 key
        if (fileName != null && !fileName.isEmpty()) {
            minioService.removeObject("image", fileName);
        }

        // 删除数据库记录
        return animeCarouselMapper.deleteById(id) > 0;
    }


    //查询单个轮播图
    @Override
    public AnimeCarousel getCarouselById(Long id) {
        return animeCarouselMapper.selectById(id);
    }
}
