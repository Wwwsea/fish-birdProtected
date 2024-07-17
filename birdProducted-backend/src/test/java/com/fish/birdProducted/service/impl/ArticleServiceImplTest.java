package com.fish.birdProducted.service.impl;

import com.fish.birdProducted.domain.dto.ArticleDTO;
import com.fish.birdProducted.domain.entity.Article;
import com.fish.birdProducted.domain.vo.ArticleListVO;
import com.fish.birdProducted.mapper.ArticleMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author:fish
 * @date: 2024/4/21-22:54
 * @content:
 */
@SpringBootTest
class ArticleServiceImplTest {

    @Autowired
    private ArticleServiceImpl articleService;
    @Resource
    private ArticleMapper articleMapper;
    @Test
    void publish() {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setArticleCover("test ES 000004");
        articleDTO.setArticleTitle("test ES 000004");
        articleDTO.setArticleContent("test ES 000004");
        articleDTO.setArticleType(1);
        articleDTO.setCategoryId(11L);
//        articleDTO.setId(50L);
        articleDTO.setIsTop(1);

        List<Long> list = new ArrayList<>();
        list.add(19L);
        articleDTO.setTagId(list);
        articleDTO.setStatus(1);
        articleService.publish(articleDTO);
    }

    @Test
    void deleteArticle(){
        List<Long> ids = new ArrayList<>();
        ids.add(51L);
        ids.add(52L);
        articleService.deleteArticle(ids);
    }

    @Test
    void listArticle(){
//        List<ArticleListVO> res = articleService.listArticle();
//        List<Long> res = articleMapper.selectList(null).stream().map(Article::getId).toList();
        Object[] resArray = articleMapper.selectList(null).toArray();
        List<Long> res = Arrays.stream(resArray)
                .map(obj -> {
                    Article article = (Article) obj;
                    return article.getId();
                })
                .collect(Collectors.toList());
        System.out.println(res.toArray());
    }

}