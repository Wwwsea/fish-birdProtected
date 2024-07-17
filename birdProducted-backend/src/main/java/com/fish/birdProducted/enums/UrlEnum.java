package com.fish.birdProducted.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author fish
 * <p>
 * 创建时间：2024/2/25 9:20
 */
@Getter
@AllArgsConstructor
public enum UrlEnum {

    /**
     * Gitee解析Token获取个人信息
     */
    GITEE_USER_INFO("https://gitee.com/api/v5/user", "GET", "Gitee解析Token获取个人信息");

    /**
     * url
     */
    private final String url;

    /**
     * 请求方法
     */
    private final String method;

    /**
     * 描述
     */
    private final String description;
}
