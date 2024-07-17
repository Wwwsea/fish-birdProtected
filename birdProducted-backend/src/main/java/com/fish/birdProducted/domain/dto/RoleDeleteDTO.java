package com.fish.birdProducted.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author fish
 * <p>
 * 创建时间：2024/2/4 9:42
 */
@Data
public class RoleDeleteDTO {
    @NotNull
    private List<Long> Ids;
}
