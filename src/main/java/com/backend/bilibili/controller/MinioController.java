package com.backend.bilibili.controller;



import com.backend.bilibili.pojo.UploadResponse;
import com.backend.bilibili.service.minio.MinioService;
import com.backend.bilibili.utils.MinioUrlUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/minio")
public class MinioController {

    private final MinioService minioService;

    public MinioController(MinioService minioService) {
        this.minioService = minioService;
    }

    @PostMapping("/upload")
    public ResponseEntity<UploadResponse> upload(@RequestParam("file") MultipartFile multipartFile) throws Exception {

        String fileName = minioService.putObject(multipartFile);

        String url = MinioUrlUtil.getUrl(fileName);

        UploadResponse response = new UploadResponse(fileName, url);

        return ResponseEntity.ok(response);
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

    @GetMapping("/presigned-get-url")
    public Map<String, Object> getPresignedGetUrl(@RequestParam String filename) {
        String url = minioService.generatePresignedGetUrl(filename, 3600); // 1小时
        Map<String, Object> result = new HashMap<>();
        result.put("error_message", "success");
        result.put("url", url);
        return result;
    }

    @GetMapping("/presigned-put-url")
    public Map<String, Object> getPresignedPutUrl(@RequestParam String filename) {
        String url = minioService.generatePresignedPutUrl(filename, 3600); // 1小时
        Map<String, Object> result = new HashMap<>();
        result.put("error_message", "success");
        result.put("url", url);
        return result;

    }
}

