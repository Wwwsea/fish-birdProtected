package com.fish.birdProducted.utils;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.fish.birdProducted.constants.Const;
import com.fish.birdProducted.enums.UploadEnum;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @author fish
 * <p>
 * 创建时间：2024/2/26 19:16
 * 文件上传工具类
 */
@Slf4j
@Component
public class FileUploadUtils {

    @Resource
    private MinioClient client;

    @Value("${minio.bucketName}")
    private String bucketName;

    @Value("${minio.endpoint}")
    private String endpoint;

    /**
     * 上传文件
     *
     * @param uploadEnum 文件枚举
     * @param file      文件
     * @return 上传后的文件地址
     * @throws Exception 异常
     */
    public String upload(UploadEnum uploadEnum,MultipartFile file) throws Exception {
        if (isFormatFile(file.getOriginalFilename(),uploadEnum.getFormat())){
            InputStream stream = file.getInputStream();
            String name = UUID.randomUUID().toString();
            String originalFilename = file.getOriginalFilename();
            String fileExtension = null;
            if (originalFilename != null) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            }
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .headers(Map.of(Const.CONTENT_TYPE, Objects.requireNonNull(file.getContentType())))
                    .object(uploadEnum.getDir() + name + "." + fileExtension)
                    .stream(stream, file.getSize(), -1)
                    .build();
            client.putObject(args);
            return endpoint + "/" + bucketName + "/" + uploadEnum.getDir() + name + "." + fileExtension;
        }
        log.error("--------------------上传文件格式不正确--------------------");
        return null;
    }

    /**
     * TODO 文件夹上传部分
     * @param uploadEnum
     * @param fileOrDir
     * @return {@link String }
     * @throws Exception
     */
    public String uploadFile(UploadEnum uploadEnum, File fileOrDir) throws Exception {
        // 检查上传的文件或文件夹是否符合格式要求
        if (isFormatFile(String.valueOf(fileOrDir), uploadEnum.getFormat())) {
            // 生成上传对象的名称和路径
            String name = UUID.randomUUID().toString();
            String fileExtension = getFileExtension(fileOrDir);

            // 根据上传的是文件还是文件夹，设置不同的上传参数
            if (fileOrDir.isDirectory()) {
                // 文件夹上传
                String objectKey = uploadEnum.getDir() + name + "/";
                uploadDirectory(fileOrDir, objectKey);
                return endpoint + "/" + bucketName + "/" + objectKey;
            } else {
                // 单个文件上传
                InputStream stream = new FileInputStream(fileOrDir);
                PutObjectArgs args = PutObjectArgs.builder()
                        .bucket(bucketName)
                        .headers(Map.of(Const.CONTENT_TYPE, getContentType(fileExtension)))
                        .object(uploadEnum.getDir() + name + "." + fileExtension)
                        .stream(stream, fileOrDir.length(), -1)
                        .build();
                client.putObject(args);
                return endpoint + "/" + bucketName + "/" + uploadEnum.getDir() + name + "." + fileExtension;
            }
        } else {
            log.error("--------------------上传文件格式不正确--------------------");
            return null;
        }
    }

    // 获取文件扩展名
    private String getFileExtension(File file) {
        String originalFilename = file.getName();
        return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
    }

    // 获取文件内容类型
    private String getContentType(String fileExtension) {
        // 这里需要根据文件扩展名来返回相应的内容类型，例如：application/zip、image/jpeg等
        // 如果不确定文件类型，可以使用默认的 application/octet-stream
        return "application/octet-stream";
    }

    // 上传文件夹
    private void uploadDirectory(File directory, String objectKeyPrefix) throws Exception {
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isDirectory()) {
                // 递归上传子文件夹
                uploadDirectory(file, objectKeyPrefix + file.getName() + "/");
            } else {
                // 上传单个文件
                InputStream stream = new FileInputStream(file);
                PutObjectArgs args = PutObjectArgs.builder()
                        .bucket(bucketName)
                        .headers(Map.of(Const.CONTENT_TYPE, getContentType(getFileExtension(file))))
                        .object(objectKeyPrefix + file.getName())
                        .stream(stream, file.length(), -1)
                        .build();
                client.putObject(args);
            }
        }
    }

    /**
     * 获取目录下的所有文件名称
     *
     * @param dir 目录
     * @return 所有文件全路径名称
     */
    public List<String> listFiles(String dir) {
        // 测试
        dir = dir.endsWith("/") ? dir : dir + "/";
        ListObjectsArgs listObjectsArgs = ListObjectsArgs.builder()
                .bucket(bucketName)
                .prefix(dir)
                .build();
        Iterable<Result<Item>> results = client.listObjects(listObjectsArgs);

        List<String> fileNames = new ArrayList<>();
        results.forEach(result -> {
            Item item;
            try {
                // 提取出文件名
                item = result.get();
                fileNames.add(item.objectName());
            } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                     InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                     XmlParserException e) {
                log.error("获取文件出现错误", e);
            }
        });

        return fileNames;
    }

    /**
     * 批量删除
     * @param fileNames 文件名称
     * @return 是否成功
     * @throws Exception 异常
     */
    public boolean deleteFiles(List<String> fileNames) throws Exception {
        List<DeleteObject> deleteObjects = fileNames.stream().map(DeleteObject::new).toList();
        RemoveObjectsArgs removeObjectsArgs = RemoveObjectsArgs.builder()
                .bucket(bucketName)
                .objects(deleteObjects)
                .build();
        Iterable<Result<DeleteError>> results = client.removeObjects(removeObjectsArgs);
        for (Result<DeleteError> result : results) {
            DeleteError error = result.get();
            log.error("文件: " + error.objectName() + "删除错误; ", error.message());
            return false;
        }
        return true;
    }

    /**
     * 文件格式校验
     * @param fileName 文件名称
     * @param format 支持的后辍
     * @return 是否支持
     */
    public boolean isFormatFile(String fileName,List<String> format) {
        for (String s : format) {
            if (fileName.endsWith(s)) {
                return true;
            }
        }
        return false;
    }
}
