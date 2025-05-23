package com.backend.bilibili.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

@Configuration
public class MinioConfig {

    @Value("${minio.url}")
    private String url;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Bean
    public MinioClient minioClient() {
        Assert.hasText(url, "Minio url不能为空");
        Assert.hasText(accessKey, "Minio accessKey不能为空");
        Assert.hasText(secretKey, "Minio secretKey不能为空");
        return new MinioClient(url, accessKey, secretKey);
    }
}
