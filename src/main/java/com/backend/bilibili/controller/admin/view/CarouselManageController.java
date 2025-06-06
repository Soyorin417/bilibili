package com.backend.bilibili.controller.admin.view;

import com.backend.bilibili.pojo.carousel.Carousel;
import com.backend.bilibili.service.carousel.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/carousel")
public class CarouselManageController {

    @Autowired
    private CarouselService carouselService;

    @PostMapping("/add")
    public Map<String, Object> addCarousel(@RequestPart("image") MultipartFile image,
                                           @RequestParam(value = "type", defaultValue = "default") String type) {
        return carouselService.addCarousel(image, type);
    }

    @GetMapping("/all")
    public List<Carousel> getAllCarousels() {
        return carouselService.getAllCarousels();
    }

    @DeleteMapping("/delete/{id}")
    public Map<String, Object> deleteCarousel(@PathVariable Integer id) {
        boolean success = carouselService.deleteCarouselById(id);
        Map<String, Object> result = new HashMap<>();
        result.put("error_message", success ? "success" : "删除失败");
        return result;
    }

    @PutMapping("/update")
    public Map<String, Object> updateCarousel(@RequestParam("id") Integer id,
                                              @RequestPart(value = "image", required = false) MultipartFile image,
                                              @RequestParam(value = "type", required = false) String type) {
        return carouselService.updateCarousel(id, image, type);
    }


}