package com.fish.birdProducted.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author fish
 * <p>
 * 创建时间：2024/3/7 19:46
 */
@Data
@Builder
@Schema(name = "ChatDTO", description = "聊天")
public class ChatDTO {
    @NotNull
    @Schema(description = "聊天内容")
    private List<ContentDTO> content;
    @NotNull
    @Schema(description = "模型")
    private String model;
}