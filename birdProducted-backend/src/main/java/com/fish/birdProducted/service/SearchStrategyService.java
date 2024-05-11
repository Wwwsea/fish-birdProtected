package com.fish.birdProducted.service;

import com.fish.birdProducted.domain.vo.ArticleSearchVO;

import java.util.List;

/**
 * @author:fish
 * @date: 2024/4/1-21:59
 * @content: 文章搜索策略
 */

public interface SearchStrategyService {

    /**
     * 搜索文章
     *
     * @param keyword 关键字
     * @return {@link List<  ArticleSearchVO  >} 文章列表
     */
    List<ArticleSearchVO> searchArticle(String keyword);
}
