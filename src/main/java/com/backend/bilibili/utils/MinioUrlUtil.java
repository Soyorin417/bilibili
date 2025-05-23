package com.backend.bilibili.utils;

public class MinioUrlUtil {

    private static final String MINIO_ENDPOINT = "http://113.45.69.13:9000";
    private static final String BUCKET_NAME = "image";

    /**
     * 根据文件名生成访问URL
     * @param fileName 文件名，可以包含子路径，比如 "videos/2025/05/video123.mp4" 或 "covers/2025/05/cover123.jpg"
     * @return 访问URL
     */
    public static String getUrl(String fileName) {
        return String.format("%s/%s/%s", MINIO_ENDPOINT, BUCKET_NAME, fileName);
    }
}
