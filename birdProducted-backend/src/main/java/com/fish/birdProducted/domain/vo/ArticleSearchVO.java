package com.fish.birdProducted.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
//import org.springframework.data.elasticsearch.annotations.Document;
/**
 * @author:fish
 * @date: 2024/4/1-22:00
 * @content: 文章搜索Response
 */

/**
 * @author ican
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ArticleSearchVO", description = "文章搜索Response")
//@Document(indexName = "article")
public class ArticleSearchVO {

    /**
     * 文章id
     */
    @Schema(description = "文章id")
    private Long id;

    /**
     * 文章标题
     */
    @Schema(description = "文章标题")
    private String articleTitle;

    /**
     * 文章内容
     */
    @Schema(description = "文章内容")
    private String articleContent;

    /**
     * 是否删除
     */
    @Schema(description = "是否删除")
    private Integer isDeleted;

    /**
     * 文章状态 : 1公开 2私密 3草稿
     */
    @Schema(description = "文章状态")
    private Integer status;

}