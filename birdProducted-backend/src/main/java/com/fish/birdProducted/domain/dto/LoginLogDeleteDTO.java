package com.fish.birdProducted.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author fish
 * <p>
 * 创建时间：2024/2/11 20:15
 */
@Data
public class LoginLogDeleteDTO {
    @NotNull
    List<Long> Ids;
}
