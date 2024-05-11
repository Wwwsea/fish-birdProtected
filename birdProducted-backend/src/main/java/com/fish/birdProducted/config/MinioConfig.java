package com.fish.birdProducted.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fish
 * <p>
 * 创建时间：2023/2/26 16:01
 */
@Configuration
public class MinioConfig {

    /**
     * 访问地址
     */
    @Value("${minio.endpoint}")
    private String endpoint;

    /**
     * accessKey类似于用户ID，用于唯一标识你的账户
     */
    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    /**
     * 默认存储桶
     */
    @Value("${minio.bucketName}")
    private String bucketName;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}
