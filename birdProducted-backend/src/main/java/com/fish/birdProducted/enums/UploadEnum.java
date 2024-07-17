package com.fish.birdProducted.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

/**
 * @author fish
 * <p>
 * 创建时间：2024/2/27 14:20
 * 文件上传枚举
 */
@Getter
@AllArgsConstructor
public enum UploadEnum {

    // 鸟头子头像
    WEBSITE_INFO_AVATAR("websiteInfo/avatar/", "鸟头子头像", List.of("jpg", "jpeg", "png")),
    // 鸟头子背景
    WEBSITE_INFO_BACKGROUND("websiteInfo/background/", "鸟头子背景", List.of("jpg", "jpeg", "png")),
    // 鸟信息封面
    ARTICLE_COVER("article/articleCover/", "鸟信息封面", List.of("jpg", "jpeg", "png")),
    // 鸟美图
    ARTICLE_IMAGE("article/articleImage/", "鸟新鲜美图", List.of("jpg", "jpeg", "png","gif")),
    // 3D图像license.txt, scene.bin,scene.gltf,
    IMAGE_3D("article/threeD/", "3D图像", List.of("png", "txt", "bin","gltf","dir","zip"));




    // 上传目录
    private final String dir;

    // 描述
    private final String description;

    // 支持的格式
    private final List<String> format;
}
