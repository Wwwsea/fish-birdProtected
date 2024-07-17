package com.fish.birdProducted.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author fish
 * <p>
 * 创建时间：2024/2/17 9:17
 * 注册类型枚举
 */
@Getter
@AllArgsConstructor
public enum RegisterTypeEnum {

    /**
     * 邮箱或用户名登录
     */
    EMAIL(0, "邮箱登录", "email"),
    /**
     * Gitee
     */
    GITEE(1, "Gitee登录", "gitee");


    /**
     * 注册方式
     */
    private final Integer registerType;

    /**
     * 描述
     */
    private final String description;

    /**
     * 策略
     */
    private final String strategy;

}
