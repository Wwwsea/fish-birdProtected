package com.fish.birdProducted.controller;

import com.fish.birdProducted.domain.dto.CommentIsCheckDTO;
import com.fish.birdProducted.domain.dto.SearchCommentDTO;
import com.fish.birdProducted.domain.dto.UserCommentDTO;
import com.fish.birdProducted.domain.response.ResponseResult;
import com.fish.birdProducted.domain.vo.ArticleCommentVO;
import com.fish.birdProducted.domain.vo.CommentListVO;
import com.fish.birdProducted.domain.vo.PageVO;
import com.fish.birdProducted.service.CommentService;
import com.fish.birdProducted.utils.ControllerUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.fish.birdProducted.annotation.AccessLimit;
import com.fish.birdProducted.annotation.LogAnnotation;
import com.fish.birdProducted.constants.LogConst;

import java.util.List;

/**
 * @author fish
 * <p>
 * 创建时间：2024/2/3 10:12
 */
@RestController
@Tag(name = "评论相关接口")
@RequestMapping("/comment")
@Validated
public class CommentController {

    @Resource
    private CommentService commentService;

    @Operation(summary = "获取评论")
    @Parameters({
            @Parameter(name = "type", description = "评论类型", required = true),
            @Parameter(name = "typeId", description = "评论id", required = true),
            @Parameter(name = "pageNum", description = "页码", required = true),
            @Parameter(name = "pageSize", description = "每页数量", required = true)
    })
    @AccessLimit(seconds = 60, maxCount = 60)
    @GetMapping("/getComment")
    public ResponseResult<PageVO<List<ArticleCommentVO>>> comment(
            @Valid @NotNull Integer type,
            @Valid @NotNull Integer typeId,
            @Valid @NotNull Integer pageNum,
            @Valid @NotNull Integer pageSize
    ) {
        return ControllerUtils.messageHandler((() -> commentService.getComment(type, typeId, pageNum, pageSize)));
    }

    @Operation(summary = "用户添加评论")
    @Parameter(name = "commentDTO", description = "评论信息", required = true)
    @AccessLimit(seconds = 60, maxCount = 10)
    @PostMapping("/auth/add/comment")
    public ResponseResult<Void> userComment(@Valid @RequestBody UserCommentDTO commentDTO) {
        return commentService.userComment(commentDTO);
    }

    @PreAuthorize("hasAnyAuthority('bird:comment:list')")
    @Operation(summary = "后台评论列表")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module="评论管理",operation= LogConst.GET)
    @GetMapping("/back/list")
    public ResponseResult<List<CommentListVO>> backList() {
        return ControllerUtils.messageHandler(() -> commentService.getBackCommentList(null));
    }

    @PreAuthorize("hasAnyAuthority('bird:comment:search')")
    @Operation(summary = "搜索后台评论列表")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module="评论管理",operation= LogConst.SEARCH)
    @PostMapping("/back/search")
    public ResponseResult<List<CommentListVO>> backList(@RequestBody SearchCommentDTO searchDTO) {
        return ControllerUtils.messageHandler(() -> commentService.getBackCommentList(searchDTO));
    }

    @PreAuthorize("hasAnyAuthority('bird:comment:isCheck')")
    @Operation(summary = "修改评论是否通过")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module="评论管理",operation= LogConst.UPDATE)
    @PostMapping("/back/isCheck")
    public ResponseResult<Void> isCheck(@RequestBody @Valid CommentIsCheckDTO commentIsCheckDTO) {
        return commentService.isCheckComment(commentIsCheckDTO);
    }

    @PreAuthorize("hasAnyAuthority('bird:comment:delete')")
    @Operation(summary = "删除评论")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module="评论管理",operation= LogConst.DELETE)
    @DeleteMapping("/back/delete/{id}")
    public ResponseResult<Void> delete(@PathVariable("id") Long id) {
        return commentService.deleteComment(id);
    }
}
