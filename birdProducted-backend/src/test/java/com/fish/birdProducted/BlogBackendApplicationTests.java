package com.fish.birdProducted;

import com.fish.birdProducted.domain.vo.ArticleSearchVO;
import com.fish.birdProducted.service.impl.ElasticsearchServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetAddress;

//@SpringBootTest
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
        String osName = System.getProperty("os.name");

        InetAddress localHost = InetAddress.getLocalHost();

        System.out.println("Local Host IP Address: " + localHost.getHostAddress());

        System.out.println("Operating System: " + osName);
    }


}
