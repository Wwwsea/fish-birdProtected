package com.fish.birdProducted.domain.dto;

import lombok.Data;

/**
 * @author fish
 * <p>
 * 创建时间：2024/1/18 14:38
 */
@Data
public class SearchBirdCategoryDTO {
    //标签名称
    private String categoryName;
    // 开始时间
    private String startTime;
    // 结束时间
    private String endTime;



}
