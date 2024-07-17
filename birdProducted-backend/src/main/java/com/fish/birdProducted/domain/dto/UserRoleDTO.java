package com.fish.birdProducted.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author fish
 * <p>
 * 创建时间：2024/2/5 11:37
 */
@Data
public class UserRoleDTO {
    @NotNull(message = "角色不能为空")
    private Long roleId;
    @NotNull(message = "选择的用户不能为空")
    private List<Long> userId;
}
