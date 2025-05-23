package com.backend.bilibili.controller;



import com.backend.bilibili.service.minio.MinioService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/minio")
public class MinioController {

    private final MinioService minioService;

    public MinioController(MinioService minioService) {
        this.minioService = minioService;
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        return minioService.putObject(multipartFile);
    }

    @GetMapping("/download")
    public void download(@RequestParam("fileName") String fileName, HttpServletResponse response) {
        minioService.download(fileName, response);
    }

    @GetMapping("/buckets")
    public List<String> listBuckets() throws Exception {
        return minioService.listBucketNames();
    }

    @PostMapping("/bucket")
    public boolean createBucket(@RequestParam String bucketName) throws Exception {
        return minioService.makeBucket(bucketName);
    }

    @DeleteMapping("/bucket")
    public boolean deleteBucket(@RequestParam String bucketName) throws Exception {
        return minioService.removeBucket(bucketName);
    }

    @GetMapping("/objects")
    public List<String> listObjectNames(@RequestParam String bucketName) throws Exception {
        return minioService.listObjectNames(bucketName);
    }

    @DeleteMapping("/object")
    public boolean removeObject(@RequestParam String bucketName, @RequestParam String objectName) throws Exception {
        return minioService.removeObject(bucketName, objectName);
    }

    @GetMapping("/object/url")
    public String getObjectUrl(@RequestParam String bucketName, @RequestParam String objectName) throws Exception {
        return minioService.getObjectUrl(bucketName, objectName);
    }
}

