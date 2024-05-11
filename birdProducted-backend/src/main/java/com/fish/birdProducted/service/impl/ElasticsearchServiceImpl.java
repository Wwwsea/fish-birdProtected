package com.fish.birdProducted.service.impl;

/**
 * @author:fish
 * @date: 2024/4/1-15:24
 * @content:
 */
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import com.fish.birdProducted.constants.ElasticConst;
import com.fish.birdProducted.domain.vo.ArticleSearchVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Slf4j
@Service
public class ElasticsearchServiceImpl {

    @Resource
    private ElasticsearchClient elasticsearchClient;

    /**
     * @params: article
     *
     * @return:  void
     * @description: 写入数据到 article 索引中
    */
    public void addArticle(ArticleSearchVO article) {
        try {
            IndexRequest<ArticleSearchVO> indexRequest = IndexRequest.of(request -> request
                    .index(ElasticConst.ARTICLE_INDEX)
                    .id(article.getId().toString())
                    .document(article));
            elasticsearchClient.index(indexRequest);
        } catch (IOException e) {
            log.error("ElasticsearchService.addArticle fail, {}", e.getMessage());
        }
    }

    public void updateArticle(ArticleSearchVO article) {
        try {
            elasticsearchClient.update(request -> request
                    .index(ElasticConst.ARTICLE_INDEX)
                    .id(article.getId().toString())
                    .doc(article), ArticleSearchVO.class);
        } catch (IOException e) {
            log.error("ElasticsearchService.updateArticle fail, {}", e.getMessage());
        }
    }

    public void deleteArticle(Long id) {
        try {
            elasticsearchClient.delete(
                    deleteRequest -> deleteRequest
                            .index(ElasticConst.ARTICLE_INDEX)
                            .id(id.toString()));
        } catch (IOException e) {
            log.error("ElasticsearchService.deleteArticle fail, {}", e.getMessage());
        }
    }
}