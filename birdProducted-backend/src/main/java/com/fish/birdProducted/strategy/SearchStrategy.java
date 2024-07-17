package com.fish.birdProducted.strategy;

import com.fish.birdProducted.domain.vo.ArticleSearchVO;

import java.util.List;

/**
 * @author fish
 * @date 2024/4/8-10:47
 * @content 文章搜索策略
 */

public interface SearchStrategy {

    /**
     * 搜索文章
     *
     * @param keyword 关键字
     * @return {@link List<ArticleSearchVO>} 文章列表
     */
    List<ArticleSearchVO> searchArticle(String keyword);
}
