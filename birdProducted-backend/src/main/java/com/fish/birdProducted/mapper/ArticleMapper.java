package com.fish.birdProducted.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fish.birdProducted.domain.entity.Article;
import com.fish.birdProducted.domain.vo.ArticleSearchVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * (Article)表数据库访问层
 *
 * @author fish
 * @since 2024-2-15 02:29:11
 */
public interface ArticleMapper extends BaseMapper<Article> {
    /**
     * 文章搜索
     *
     * @param keyword 关键字
     * @return 文章列表
     */
    List<ArticleSearchVO> searchArticle(@Param("keyword") String keyword);

}
