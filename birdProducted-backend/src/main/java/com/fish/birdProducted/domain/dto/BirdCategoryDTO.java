package com.fish.birdProducted.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import com.fish.birdProducted.domain.BaseData;

/**
 * @author:fish
 * @date: 2024/3/15-14:46
 * @content:
 */

@Accessors(chain = true)
@Data
public class BirdCategoryDTO implements BaseData {
    //分类id
    private Long id;
    //分类名
    @NotBlank(message = "分类名称不能为空")
    @Length(max = 20, message = "分类名称长度不能超过20")
    private String categoryName;


    @NotBlank(message = "所属生物分支不能为空")
    @Length(max = 20, message = "分支长度不能超过20")
    private String biologyBranch;

    // 所属父id
    private int parentId;
}
