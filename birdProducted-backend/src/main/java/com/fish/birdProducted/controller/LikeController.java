package com.fish.birdProducted.controller;

import com.fish.birdProducted.domain.entity.Like;
import com.fish.birdProducted.domain.response.ResponseResult;
import com.fish.birdProducted.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.fish.birdProducted.annotation.AccessLimit;

import java.util.List;

/**
 * @author fish
 * <p>
 * 创建时间：2023/11/3 10:15
 */
@RestController
@Tag(name = "评论相关接口")
@RequestMapping("/like")
@Validated
public class LikeController {

    @Resource
    private LikeService likeService;

    @Operation(summary = "点赞")
    @Parameters({
            @Parameter(name = "type", description = "点赞类型", required = true),
            @Parameter(name = "typeId", description = "点赞id", required = true)
    })
    @AccessLimit(seconds = 60, maxCount = 10)
    @PostMapping("/auth/like")
    public ResponseResult<Void> like(
            @RequestParam("type") @Valid @NotNull Integer type,
            @RequestParam("typeId") @Valid @NotNull Integer typeId
    ) {
        return likeService.userLike(type, typeId);
    }

    @Operation(summary = "取消点赞")
    @Parameters({
            @Parameter(name = "type", description = "点赞类型", required = true),
            @Parameter(name = "typeId", description = "点赞id", required = true)
    })
    @AccessLimit(seconds = 60, maxCount = 10)
    @DeleteMapping("/auth/like")
    public ResponseResult<Void> cancelLike(
            @RequestParam("type") @Valid @NotNull Integer type,
            @RequestParam("typeId") @Valid @NotNull Integer typeId
    ) {
        return likeService.cancelLike(type, typeId);
    }

    @Operation(summary = "是否已经点赞")
    @Parameters({
            @Parameter(name = "type", description = "点赞类型", required = true),
            @Parameter(name = "typeId", description = "点赞id", required = true)
    })
    @AccessLimit(seconds = 60, maxCount = 60)
    @GetMapping("whether/like")
    public ResponseResult<List<Like>> isLike(
            @Valid @NotNull @RequestParam("type") Integer type,
            @RequestParam(value = "typeId", required = false) Integer typeId
    ) {
        return likeService.isLike(type, typeId);
    }

}
