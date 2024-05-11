package com.fish.birdProducted.strategy;

/**
 * @author: fish
 * @date: 2024/4/8-13:23
 * @content: ES搜索策略
 */

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.fish.birdProducted.constants.Const;
import com.fish.birdProducted.constants.ElasticConst;
import com.fish.birdProducted.domain.vo.ArticleSearchVO;
import com.fish.birdProducted.enums.ArticleStatusEnum;
import com.fish.birdProducted.service.ArticleService;
import com.fish.birdProducted.service.impl.ArticleServiceImpl;
import lombok.extern.log4j.Log4j2;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static co.elastic.clients.elasticsearch._types.SearchType.QueryThenFetch;

@Log4j2
@Service("esSearchStrategyImpl")
public class EsSearchStrategyImpl implements SearchStrategy {

    @Autowired
    private ElasticsearchClient elasticsearchClient;






    /**
     * @params: keyword: 查询关键字
     *
     * @return: ArticleSearchVO
     * @description:
     * 匹配关键字的字段：all 字段中包含了所有文章内容，通过 match 查询进行匹配，排除已删除的文章，只搜索发布状态的文章
     * 同时，还对搜索结果中的标题和内容进行了高亮处理，以突出显示关键字
    */
    public List<ArticleSearchVO> searchArticle(String keyword) {
        if (StringUtils.isBlank(keyword)) {
            System.out.println("传入关键字为空。。。");
            return new ArrayList<>();
        }
        try {
            // 条件构造
            SearchRequest searchRequest = SearchRequest.of(s -> s.index(ElasticConst.ARTICLE_INDEX)
                    .query(query -> query
                            .bool(bool -> bool
                                    .must(must -> must.match(m -> m.field("all").query(FieldValue.of(keyword))))
                                    .must(must -> must.term(m -> m.field("isDeleted").value(FieldValue.of(Const.FALSE))))
                                    .must(must -> must.term(m -> m.field("status").value(FieldValue.of(ArticleStatusEnum.PUBLIC.getStatus())))))
                    ).highlight(h -> h
                            .fields(ElasticConst.ARTICLE_TITLE,f -> f.preTags(ElasticConst.PRE_TAG).postTags(ElasticConst.POST_TAG))
                            .fields(ElasticConst.ARTICLE_CONTENT, f -> f.preTags(ElasticConst.PRE_TAG).postTags(ElasticConst.POST_TAG))
                            .requireFieldMatch(false)
                    ));

            SearchResponse<ArticleSearchVO> search = elasticsearchClient.search(searchRequest, ArticleSearchVO.class);
            // 解析结果           l":{"relation":"eq","value":0},"hits":[],"max_score":null}}
            //        SearchResponse: {"took":2,"timed_out":false,"_shards":{"failed":0.0,"successful":1.0,"total":1.0,"skipped":0.0},"hits":{"total":{"relation":"eq","value":0},"hits":[],"max_score":null}}:

            System.out.println(search+":解析完成++++++++++++++++++++++");
//            System.out.println("解析成功后操作"+handleResponse(search));
            return handleResponse(search);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new ArrayList<>();
    }

    /**
     * @params: response
     *
     * @return:  List<ArticleSearchVO>
     * @description: 解析搜索结果，并将其转换为 ArticleSearchVO 对象列表。对于每个搜索命中的结果，它执行以下操作：
     * 如果标题字段有高亮结果，则将高亮结果设置为文章标题
     * 如果内容字段有高亮结果，则将高亮结果设置为文章内容；如果没有高亮结果，则截取文章内容的前 300 个字符作为摘要
     * 最后，将处理后的结果收集到列表中并返回
    */

    private List<ArticleSearchVO> handleResponse(SearchResponse<ArticleSearchVO> response) {
        if (response == null || response.hits() == null || response.hits().hits() == null) {
            return new ArrayList<>(111);
        }
        // 解析结果并返回
        return response.hits().hits().stream()
                .map(hit -> {
                    if (hit.source() == null) {
                        System.out.println("+++++++++++为空");
                        return null;
                    }
                    if (CollectionUtils.isNotEmpty(hit.highlight().get(ElasticConst.ARTICLE_TITLE))) {
                        Objects.requireNonNull(hit.source()).setArticleTitle(hit.highlight().get(ElasticConst.ARTICLE_TITLE).get(0));
                    }
                    if (CollectionUtils.isNotEmpty(hit.highlight().get(ElasticConst.ARTICLE_CONTENT))) {
                        Objects.requireNonNull(hit.source()).setArticleContent(hit.highlight().get(ElasticConst.ARTICLE_CONTENT).get(0));
                    } else {
                        Objects.requireNonNull(hit.source()).setArticleContent(hit.source().getArticleContent().substring(0, 300));
                    }
                    return hit.source();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
