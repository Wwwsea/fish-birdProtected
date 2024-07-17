package com.fish.birdProducted.service;

/**
 * @author fish
 * <p>
 * 创建时间：2024/2/22 15:17
 */
public interface RedisService {

    void articleCountClear();

    void articleVisitCount();
}
