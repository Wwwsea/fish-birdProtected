package com.fish.birdProducted.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fish
 * <p>
 * 创建时间：2024/2/19 16:50
 * @Content 分页VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "PageVO", description = "分页VO")
public class PageVO<T> {

    /**
     * 数据
     */
    @Schema(description = "数据")
    private T page;
    /**
     * 数据总数
     */
    @Schema(description = "数据总数")
    private Long total;
}
