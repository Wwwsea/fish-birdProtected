package com.fish.birdProducted.strategy;

import com.fish.birdProducted.domain.vo.ArticleSearchVO;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.fish.birdProducted.enums.SearchModeEnum.getStrategy;
/**
 * @author: fish
 * @date: 2024/4/8-10:45
 * @content: 搜索策略上下文
 */

@Service
public class SearchStrategyContext {

    /**
     * 搜索模式
     */
    @Value("${search.mode}")
    private String searchMode;

    @Resource
    private Map<String, SearchStrategy> searchStrategyMap;

    /**
     * 执行搜索策略
     *
     * @param keyword 关键字
     * @return {@link List <ArticleSearchVO>} 搜索文章
     */
    public List<ArticleSearchVO> executeSearchStrategy(String keyword) {
        return searchStrategyMap.get(getStrategy(searchMode)).searchArticle(keyword);
    }

}