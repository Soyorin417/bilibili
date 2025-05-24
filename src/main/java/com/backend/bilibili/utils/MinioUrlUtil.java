package com.backend.bilibili.utils;

public class MinioUrlUtil {

    private static final String MINIO_ENDPOINT = "http://113.45.69.13:9000";
    private static final String BUCKET_NAME = "image";

    public static String getUrl(String fileName) {
        return String.format("%s/%s/%s", MINIO_ENDPOINT, BUCKET_NAME, fileName);
    }
}
