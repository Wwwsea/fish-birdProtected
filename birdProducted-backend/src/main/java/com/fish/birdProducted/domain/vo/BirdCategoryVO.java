package com.fish.birdProducted.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;


/**
 * @author fish
 * <p>
 * 创建时间：2023/10/27 14:53
 * 分类VO
 */
@Data
@Schema(name = "BirdCategoryVO", description = "分类VO")
public class BirdCategoryVO {
    //分类id
    @Schema(description = "分类id")
    private Long id;
    //分类名
    @Schema(description = "分类名")
    private String categoryName;
    // 分类下的文章数量
    @Schema(description = "分类下的文章数量")
    private Long articleCount;

    @Schema(description = "生物分支")
    private String biologyBranch;

    @Schema(description = "所属父id")
    private int parentId;

    // 标签创建时间
    private Date createTime;
    // 标签更新时间
    private Date updateTime;

}
