package com.fish.birdProducted.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author fish
 * <p>
 * 创建时间：2024/2/17 20:23
 * 相关文章VO
 */
@Data
@Schema(name = "RelatedArticleVO", description = "相关文章VO")
public class RelatedArticleVO {
    //文章id
    @Schema(description = "文章id")
    private Long id;
    //文章缩略图
    @Schema(description = "文章缩略图")
    private String articleCover;
    //文章标题
    @Schema(description = "文章标题")
    private String articleTitle;
    //文章创建时间
    @Schema(description = "文章创建时间")
    private Date createTime;
}
