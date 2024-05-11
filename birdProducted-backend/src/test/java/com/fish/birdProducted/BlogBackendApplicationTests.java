package com.fish.birdProducted;

import com.fish.birdProducted.domain.dto.BirdCategoryDTO;
import com.fish.birdProducted.domain.dto.CategoryDTO;
import com.fish.birdProducted.domain.dto.SearchBirdCategoryDTO;
import com.fish.birdProducted.domain.dto.SearchCategoryDTO;
import com.fish.birdProducted.domain.vo.ArticleSearchVO;
import com.fish.birdProducted.domain.vo.BirdCategoryVO;
import com.fish.birdProducted.domain.vo.CategoryVO;
import com.fish.birdProducted.service.impl.ArticleServiceImpl;
import com.fish.birdProducted.service.impl.BirdCategoryServiceImpl;
import com.fish.birdProducted.service.impl.CategoryServiceImpl;
import com.fish.birdProducted.service.impl.ElasticsearchServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.fish.birdProducted.utils.ParseEmailUtils;

import java.util.List;

@SpringBootTest
class  BlogBackendApplicationTests {

    @Autowired
    private ElasticsearchServiceImpl elasticsearchService;




    @Test
    void contextLoads() throws Exception {
        ArticleSearchVO article1 = new ArticleSearchVO();
        article1.setId(46L);
        article1.setIsDeleted(0);

        elasticsearchService.updateArticle(article1);

//        List<ArticleSearchVO> searchVOS = articleService.listArticlesBySearch("test");
//        System.out.println(searchVOS);

    }

    @Test
    void esLoads() throws Exception {

    }

}
