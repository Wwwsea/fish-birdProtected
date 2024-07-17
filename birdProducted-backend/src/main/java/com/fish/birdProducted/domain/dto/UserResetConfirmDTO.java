package com.fish.birdProducted.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author fish
 * <p>
 * 创建时间：2024/2/17 15:09
 */
@Data
public class UserResetConfirmDTO {
    //验证码
    @Schema(description = "验证码")
    @Length(max = 6, min = 6)
    private String code;
    // 邮箱
    @Schema(description = "邮箱")
    @Email
    @Length(min = 4)
    private String email;
}
