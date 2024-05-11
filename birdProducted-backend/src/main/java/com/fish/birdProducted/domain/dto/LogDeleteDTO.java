package com.fish.birdProducted.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author fish
 * <p>
 * 创建时间：2023/2/11 20:15
 */
@Data
public class LogDeleteDTO {
    @NotNull
    List<Long> Ids;
}
