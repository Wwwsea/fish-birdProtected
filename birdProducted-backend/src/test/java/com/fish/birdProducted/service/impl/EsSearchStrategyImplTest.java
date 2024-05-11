package com.fish.birdProducted.service.impl;

import com.fish.birdProducted.domain.vo.ArticleSearchVO;
import com.fish.birdProducted.strategy.EsSearchStrategyImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class EsSearchStrategyImplTest {

    @Autowired
    private EsSearchStrategyImpl service;

    @Test
    void searchArticle() throws Exception{
        List<ArticleSearchVO> res = service.searchArticle("test");
        System.out.println(res.toArray().length);
    }
}