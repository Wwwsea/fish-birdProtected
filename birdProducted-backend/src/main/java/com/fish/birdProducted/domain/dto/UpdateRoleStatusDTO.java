package com.fish.birdProducted.domain.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author fish
 * <p>
 * 创建时间：2024/3l/30 17:20
 */
@Data
public class UpdateRoleStatusDTO {
    @NotNull
    private Long id;
    @Min(value = 0)
    @Max(value = 1)
    private Integer status;
}
