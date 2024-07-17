package com.fish.birdProducted.controller;

import com.fish.birdProducted.annotation.LogAnnotation;
import com.fish.birdProducted.constants.LogConst;
import com.fish.birdProducted.domain.dto.BirdCategoryDTO;
import com.fish.birdProducted.domain.dto.SearchBirdCategoryDTO;
import com.fish.birdProducted.domain.response.ResponseResult;
import com.fish.birdProducted.domain.vo.BirdCategoryVO;
import com.fish.birdProducted.service.BirdCategoryService;
import com.fish.birdProducted.utils.ControllerUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.fish.birdProducted.annotation.AccessLimit;

import java.util.List;

/**
 * @author:fish
 * @date: 2024/3/15-20:42
 * @content:
 */

@RestController
@RequestMapping("/birdcategories")
@Validated
@Tag(name = "鸟树形分类相关接口")
public class BirdCategoryController {

    @Autowired
    private BirdCategoryService categoryService;

    @Operation(summary = "获取所有分类")
    @AccessLimit(seconds = 60, maxCount = 60)
    @GetMapping("/list")
    public ResponseResult<List<BirdCategoryVO>> getAllCategories() {
        return ControllerUtils.messageHandler((categoryService::listAllCategory));
    }

    @Operation(summary = "获取分类列表")
    @PreAuthorize("hasAnyAuthority('bird:category:list')")
    @LogAnnotation(module="分类管理",operation= LogConst.GET)
    @AccessLimit(seconds = 60, maxCount = 30)
    @GetMapping("/back/list")
    public ResponseResult<List<BirdCategoryVO>> listArticleCategory() {
        return ControllerUtils.messageHandler((categoryService::listAllCategory));
    }

    @Operation(summary = "搜索分类列表")
    @PreAuthorize("hasAnyAuthority('bird:category:search')")
    @LogAnnotation(module="分类管理",operation= LogConst.SEARCH)
    @AccessLimit(seconds = 60, maxCount = 30)
    @PostMapping("/back/search")
    public ResponseResult<List<BirdCategoryVO>> searchCategory(@RequestBody SearchBirdCategoryDTO searchCategoryDTO) {
        return ControllerUtils.messageHandler(() -> categoryService.searchBirdCategory(searchCategoryDTO));
    }

    @Operation(summary = "根据id查询分类")
    @PreAuthorize("hasAnyAuthority('bird:category:search')")
    @LogAnnotation(module="分类管理",operation= LogConst.GET)
    @AccessLimit(seconds = 60, maxCount = 30)
    @GetMapping("/back/get/{id}")
    public ResponseResult<BirdCategoryVO> getCategoryById(@PathVariable(value = "id") Long id) {
        return ControllerUtils.messageHandler(() -> categoryService.getBirdCategoryById(id));
    }

    @Operation(summary = "新增分类-分类列表")
//    @PreAuthorize("hasAnyAuthority('bird:category:add')")
    @LogAnnotation(module="分类管理",operation= LogConst.INSERT)
    @AccessLimit(seconds = 60, maxCount = 30)
    @PutMapping("/back/add")
    public ResponseResult<Void> addOrUpdateCategory(@RequestBody @Valid BirdCategoryDTO categoryDTO) {
        return categoryService.addOrUpdateBirdCategory(categoryDTO.setId(null));
    }

    @Operation(summary = "修改分类")
    @PreAuthorize("hasAnyAuthority('bird:category:update')")
    @LogAnnotation(module="分类管理",operation= LogConst.UPDATE)
    @AccessLimit(seconds = 60, maxCount = 30)
    @PostMapping("/back/update")
    public ResponseResult<Void> updateCategory(@RequestBody @Valid BirdCategoryDTO categoryDTO) {
        return categoryService.addOrUpdateBirdCategory(categoryDTO);
    }

    @Operation(summary = "删除分类")
    @PreAuthorize("hasAnyAuthority('bird:category:delete')")
    @LogAnnotation(module="分类管理",operation= LogConst.DELETE)
    @AccessLimit(seconds = 60, maxCount = 30)
    @DeleteMapping("/back/delete")
    public ResponseResult<Void> deleteCategory(@RequestBody Long ids) {
        return categoryService.deleteBirdCategoryByIds(ids);
    }


}
