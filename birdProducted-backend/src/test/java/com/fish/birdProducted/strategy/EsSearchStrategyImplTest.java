package com.fish.birdProducted.strategy;

import com.fish.birdProducted.domain.vo.ArticleSearchVO;
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
        List<ArticleSearchVO> res = service.searchArticle("鸮");
        System.out.println(res.toArray().length);
    }
}