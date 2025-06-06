package com.backend.bilibili.service.minio;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MinioService implements InitializingBean {

    @Value("${minio.bucket}")
    private String bucket;

    @Value("${minio.url}")
    private String url;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    private MinioClient minioClient;

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.hasText(url, "Minio url 不能为空");
        Assert.hasText(accessKey, "Minio accessKey 不能为空");
        Assert.hasText(secretKey, "Minio secretKey 不能为空");

        minioClient = new MinioClient(url, accessKey, secretKey);
    }
    /**
     * 上传
     */
    public String putObject(MultipartFile multipartFile) throws Exception {
        if (!minioClient.bucketExists(this.bucket)) {
            minioClient.makeBucket(this.bucket);
        }

        // 给文件加上 UUID，防止重名
        String fileName = UUID.randomUUID().toString() + "-" + multipartFile.getOriginalFilename();

        try (InputStream inputStream = multipartFile.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(this.bucket)
                            .object(fileName)
                            .stream(inputStream, multipartFile.getSize(), -1)
                            .contentType(multipartFile.getContentType())
                            .build()
            );
        }
        System.out.println(minioClient.getObjectUrl(this.bucket, fileName));

        return minioClient.getObjectUrl(this.bucket, fileName);
    }





    /**
     * 文件下载
     */
    public void download(String fileName, HttpServletResponse response){
// 从链接中得到文件名
        InputStream inputStream;
        try {
            MinioClient minioClient = new MinioClient(url, accessKey, secretKey);
            ObjectStat stat = minioClient.statObject(bucket, fileName);
            inputStream = minioClient.getObject(bucket, fileName);
            response.setContentType(stat.contentType());
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            IOUtils.copy(inputStream, response.getOutputStream());
            inputStream.close();
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("有异常：" + e);
        }
    }

    public String uploadFile(MultipartFile file) throws Exception {
        if (!minioClient.bucketExists(bucket)) {
            minioClient.makeBucket(bucket);
        }

        try (InputStream inputStream = file.getInputStream()) {
            String fileName = file.getOriginalFilename();

            PutObjectOptions options = new PutObjectOptions(file.getSize(), PutObjectOptions.MIN_MULTIPART_SIZE);
            options.setContentType(file.getContentType());

            minioClient.putObject(bucket, fileName, inputStream, options);

            return url + "/" + bucket + "/" + UriUtils.encode(fileName, StandardCharsets.UTF_8);
        }
    }

    public void downloadFile(String fileName, HttpServletResponse response) throws Exception {
        ObjectStat stat = minioClient.statObject(bucket, fileName);
        try (InputStream inputStream = minioClient.getObject(bucket, fileName)) {
            response.setContentType(stat.contentType());
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");
            IOUtils.copy(inputStream, response.getOutputStream());
        }
    }


    /**
     * 列出所有存储桶名称
     *
     * @return
     * @throws Exception
     */
    public List<String> listBucketNames()
            throws Exception {
        List<Bucket> bucketList = listBuckets();
        List<String> bucketListName = new ArrayList<>();
        for (Bucket bucket : bucketList) {
            bucketListName.add(bucket.name());
        }
        return bucketListName;
    }

    /**
     * 查看所有桶
     *
     * @return
     * @throws Exception
     */
    public List<Bucket> listBuckets()
            throws Exception {
        return minioClient.listBuckets();
    }

    /**
     * 检查存储桶是否存在
     *
     * @param bucketName
     * @return
     * @throws Exception
     */
    public boolean bucketExists(String bucketName) throws Exception {
        boolean flag = minioClient.bucketExists(bucketName);
        if (flag) {
            return true;
        }
        return false;
    }

    /**
     * 创建存储桶
     *
     * @param bucketName
     * @return
     * @throws Exception
     */
    public boolean makeBucket(String bucketName)
            throws Exception {
        boolean flag = bucketExists(bucketName);
        if (!flag) {
            minioClient.makeBucket(bucketName);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除桶
     *
     * @param bucketName
     * @return
     * @throws Exception
     */
    public boolean removeBucket(String bucketName)
            throws Exception {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            Iterable<Result<Item>> myObjects = listObjects(bucketName);
            for (Result<Item> result : myObjects) {
                Item item = result.get();// 有对象文件，则删除失败
                if (item.size() > 0) {
                    return false;
                }
            }// 删除存储桶，注意，只有存储桶为空时才能删除成功。
            minioClient.removeBucket(bucketName);
            flag = bucketExists(bucketName);
            if (!flag) {
                return true;
            }

        }
        return false;
    }

    /**
     * 列出存储桶中的所有对象
     *
     * @param bucketName 存储桶名称
     * @return
     * @throws Exception
     */
    public Iterable<Result<Item>> listObjects(String bucketName) throws Exception {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            return minioClient.listObjects(bucketName);
        }
        return null;
    }

    /**
     * 列出存储桶中的所有对象名称
     *
     * @param bucketName 存储桶名称
     * @return
     * @throws Exception
     */
    public List<String> listObjectNames(String bucketName) throws Exception {
        List<String> listObjectNames = new ArrayList<>();
        boolean flag = bucketExists(bucketName);
        if (flag) {
            Iterable<Result<Item>> myObjects = listObjects(bucketName);
            for (Result<Item> result : myObjects) {
                Item item = result.get();
                listObjectNames.add(item.objectName());
            }
        }
        return listObjectNames;
    }

    /**
     * 删除一个对象
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @throws Exception
     */
    public boolean removeObject(String bucketName, String objectName) throws Exception {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            List<String> objectList = listObjectNames(bucketName);
            for (String s : objectList) {
                if(s.equals(objectName)){
                    minioClient.removeObject(bucketName, objectName);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 文件访问路径
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @return
     * @throws Exception
     */
    public String getObjectUrl(String bucketName, String objectName) throws Exception {
        boolean flag = bucketExists(bucketName);
        String url = "";
        if (flag) {
            url = minioClient.getObjectUrl(bucketName, objectName);
        }
        return url;
    }

    public String generatePresignedGetUrl(String objectName, int expirySeconds) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .method(Method.GET)
                            .expiry(expirySeconds) // 有效时间，单位：秒，最大604800（7天）
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("生成GET预签名URL失败: " + e.getMessage(), e);
        }
    }

    public String generatePresignedPutUrl(String objectName, int expirySeconds) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .method(Method.PUT)
                            .expiry(expirySeconds) // 有效时间，单位：秒
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("生成PUT预签名URL失败: " + e.getMessage(), e);
        }
    }
}
