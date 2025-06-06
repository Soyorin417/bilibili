package com.backend.bilibili.service.carousel.impl;


import com.backend.bilibili.mapper.carousel.CarouselMapper;
import com.backend.bilibili.pojo.carousel.Carousel;
import com.backend.bilibili.service.carousel.CarouselService;
import com.backend.bilibili.service.minio.MinioService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CarouselServiceImpl implements CarouselService {
    @Autowired
    private CarouselMapper carouselMapper;

    @Autowired
    private MinioService minioService;

    @Override
    public List<Carousel> getCarouselsByType(String type) {
        QueryWrapper<Carousel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", type);
        return carouselMapper.selectList(queryWrapper);
    }

    @Override
    public Map<String, Object> addCarousel(MultipartFile imageFile, String type) {
        Map<String, Object> result = new HashMap<>();
        if (imageFile == null || imageFile.isEmpty()) {
            result.put("error_message", "图片不能为空");
            return result;
        }

        try {
            String imageUrl = minioService.putObject(imageFile);
            Carousel carousel = new Carousel();
            carousel.setUrl(imageUrl);
            carousel.setType(type);

            int rows = carouselMapper.insert(carousel);
            if (rows <= 0) {
                result.put("error_message", "插入失败");
                return result;
            }

            result.put("error_message", "success");
            result.put("url", imageUrl);
            return result;
        } catch (Exception e) {
            result.put("error_message", "上传失败：" + e.getMessage());
            return result;
        }
    }

    @Override
    public Map<String, Object> updateCarousel(Integer id, MultipartFile imageFile, String type) {
        Map<String, Object> result = new HashMap<>();

        Carousel carousel = carouselMapper.selectById(id);
        if (carousel == null) {
            result.put("error_message", "轮播图不存在");
            return result;
        }

        try {
            // 更新图片
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

            // 更新数据库
            int rows = carouselMapper.updateById(carousel);
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


    @Override
    public boolean deleteCarouselById(Integer id) {
        return carouselMapper.deleteById(id) > 0;
    }

    @Override
    public List<Carousel> getAllCarousels() {
        return carouselMapper.selectList(null);
    }
}
