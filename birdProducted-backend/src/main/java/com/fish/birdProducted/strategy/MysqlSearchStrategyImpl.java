package com.fish.birdProducted.strategy;

import com.fish.birdProducted.constants.ElasticConst;
import com.fish.birdProducted.domain.vo.ArticleSearchVO;
import com.fish.birdProducted.mapper.ArticleMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
/**
 * @author:fish
 * @date: 2024/4/9-22:01
 * @content: MySQL搜索策略
 */

@Service("mySqlSearchStrategyImpl")
public class MysqlSearchStrategyImpl implements SearchStrategy {

    @Autowired
    private ArticleMapper articleMapper;

    public List<ArticleSearchVO> searchArticle(String keyword) {
        if (StringUtils.isBlank(keyword)) {
            return new ArrayList<>();
        }
        List<ArticleSearchVO> articleSearchVOList = articleMapper.searchArticle(keyword);
        return articleSearchVOList.stream()
                .map(article -> {
                    // 获取关键词第一次出现的位置
                    String articleContent = article.getArticleContent();
                    int index = article.getArticleContent().indexOf(keyword);
                    if (index != -1) {
                        // 获取关键词前面的文字
                        int preIndex = index > 25 ? index - 25 : 0;
                        String preText = article.getArticleContent().substring(preIndex, index);
                        // 获取关键词到后面的文字
                        int last = index + keyword.length();
                        int postLength = article.getArticleContent().length() - last;
                        int postIndex = postLength > 175 ? last + 175 : last + postLength;
                        String postText = article.getArticleContent().substring(index, postIndex);
                        // 文章内容高亮
                        articleContent = (preText + postText).replaceAll(keyword, ElasticConst.PRE_TAG + keyword + ElasticConst.POST_TAG);
                    }
                    // 文章标题高亮
                    String articleTitle = article.getArticleTitle().replaceAll(keyword, ElasticConst.PRE_TAG + keyword + ElasticConst.POST_TAG);
                    return ArticleSearchVO.builder()
                            .id(article.getId())
                            .articleTitle(articleTitle)
                            .articleContent(articleContent)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
