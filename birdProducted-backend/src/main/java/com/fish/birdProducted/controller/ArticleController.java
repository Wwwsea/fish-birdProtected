package com.fish.birdProducted.controller;

import com.fish.birdProducted.domain.dto.ArticleDTO;
import com.fish.birdProducted.domain.dto.SearchArticleDTO;
import com.fish.birdProducted.domain.response.ResponseResult;
import com.fish.birdProducted.domain.vo.*;
import com.fish.birdProducted.service.ArticleService;
import com.fish.birdProducted.service.ThreeDService;
import com.fish.birdProducted.utils.ControllerUtils;
import io.minio.Result;
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
import org.springframework.web.multipart.MultipartFile;
import com.fish.birdProducted.annotation.AccessLimit;
import com.fish.birdProducted.annotation.LogAnnotation;
import com.fish.birdProducted.constants.LogConst;

import java.util.List;

/**
 * @author fish
 * <p>
 * 创建时间：2024/10/15 2:57
 */
@RestController
@Tag(name = "文章相关接口")
@RequestMapping("/article")
@Validated
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @Resource
    private ThreeDService threeDService;

    @Operation(summary = "获取所有的文章列表")
    @AccessLimit(seconds = 60, maxCount = 10)
    @Parameters({
            @Parameter(name = "pageNum", description = "页码", required = true),
            @Parameter(name = "pageSize", description = "每页数量", required = true)
    })
    @GetMapping("/list")
    public ResponseResult<PageVO<List<ArticleVO>>> list(
            @NotNull Integer pageNum,
            @NotNull Integer pageSize
    ) {
        return ControllerUtils.messageHandler((() -> articleService.listAllArticle(pageNum, pageSize)));
    }

    @Operation(summary = "获取推荐的文章信息")
    @AccessLimit(seconds = 60, maxCount = 60)
    @GetMapping("/recommend")
    public ResponseResult<List<RecommendArticleVO>> recommend() {
        return ControllerUtils.messageHandler((() -> articleService.listRecommendArticle()));
    }

    @Operation(summary = "获取随机的文章信息")
    @AccessLimit(seconds = 60, maxCount = 60)
    @GetMapping("/random")
    public ResponseResult<List<RandomArticleVO>> random() {
        return ControllerUtils.messageHandler((() -> articleService.listRandomArticle()));
    }

    @Operation(summary = "获取文章详情")
    @Parameter(name = "id", description = "文章id", required = true)
    @AccessLimit(seconds = 60, maxCount = 60)
    @GetMapping("/detail/{id}")
    public ResponseResult<ArticleDetailVO> detail(@PathVariable("id") @NotNull Integer id) {
        return ControllerUtils.messageHandler((() -> articleService.getArticleDetail(id)));
    }
    @Operation(summary = "获取文章3D模型链接")
    @Parameter(name = "id", description = "文章id", required = true)
    @AccessLimit(seconds = 60, maxCount = 60)
    @GetMapping("/threeD/{id}")
    public ResponseResult<String> texturesImage(@PathVariable("id") @NotNull Integer id) {
        return ControllerUtils.messageHandler((() -> threeDService.texturesImage(id)));
    }

    @Operation(summary = "相关文章信息")
    @Parameters({
            @Parameter(name = "categoryId", description = "分类id", required = true),
            @Parameter(name = "articleId", description = "文章id", required = true)
    })
    @AccessLimit(seconds = 60, maxCount = 60)
    @GetMapping("/related/{categoryId}/{articleId}")
    public ResponseResult<List<RelatedArticleVO>> related(
            @PathVariable("categoryId") @NotNull Integer categoryId,
            @PathVariable("articleId") @NotNull Integer articleId
    ) {
        return ControllerUtils.messageHandler((() -> articleService.relatedArticleList(categoryId, articleId)));
    }

    @Operation(summary = "获取灭绝动物时间轴数据")
    @AccessLimit(seconds = 60, maxCount = 15)
    @GetMapping("/timeLine")
    public ResponseResult<List<TimeLineVO>> timeLine() {
        return ControllerUtils.messageHandler((articleService::listTimeLine));
    }

    @Operation(summary = "获取分类与标签下的文章")
    @Parameters({
            @Parameter(name = "typeId", description = "类型id", required = true),
            @Parameter(name = "type", description = "类型", required = true)
    })
    @AccessLimit(seconds = 60, maxCount = 60)
    @GetMapping("/where/list/{typeId}")
    public ResponseResult<List<CategoryArticleVO>> listCategoryArticle(
            @NotNull @PathVariable("typeId") Long typeId,
            @NotNull @RequestParam("type") Integer type
    ) {
        return ControllerUtils.messageHandler(() -> articleService.listCategoryArticle(type, typeId));
    }

    @Operation(summary = "文章访问量+1")
    @Parameter(name = "id", description = "文章id", required = true)
    @AccessLimit(seconds = 60, maxCount = 60)
    @GetMapping("/visit/{id}")
    public ResponseResult<Void> visit(@PathVariable("id") @NotNull Long id) {
        articleService.addVisitCount(id);
        return ControllerUtils.messageHandler(() -> null);
    }

    @PreAuthorize("hasAnyAuthority('bird:publish:article')")
    @Operation(summary = "上传鸟类3D模型")
    @Parameter(name = "threeDImage", description = "上传鸟类3D模型")
    @LogAnnotation(module = "文章管理", operation =  LogConst.UPLOAD_IMAGE)
    @AccessLimit(seconds = 60, maxCount = 5)
    @PostMapping("/upload/threeDImage")
    public ResponseResult<String> uploadThreeDImage(@RequestParam("threeDImage") MultipartFile threeDImage) {
        return threeDService.uploadThreeDImage(threeDImage);
    }

    // 上传文件 √
    @PreAuthorize("hasAnyAuthority('bird:publish:article')")
    @Operation(summary = "上传文章封面")
    @Parameter(name = "articleCover", description = "文章封面")
    @LogAnnotation(module = "文章管理", operation = LogConst.UPLOAD_IMAGE)
    @AccessLimit(seconds = 60, maxCount = 5)
    @PostMapping("/upload/articleCover")
    public ResponseResult<String> uploadArticleCover(@RequestParam("articleCover") MultipartFile articleCover) {
        return articleService.uploadArticleCover(articleCover);
    }

    @PreAuthorize("hasAnyAuthority('bird:publish:article')")
    @Operation(summary = "发布文章")
    @Parameter(name = "articleDTO", description = "文章信息")
    @LogAnnotation(module = "文章管理", operation = LogConst.INSERT)
    @AccessLimit(seconds = 60, maxCount = 30)
    @PostMapping("/publish")
    public ResponseResult<Void> publish(@RequestBody @Valid ArticleDTO articleDTO) {
        return articleService.publish(articleDTO);
    }

    @PreAuthorize("hasAnyAuthority('bird:publish:article')")
    @Operation(summary = "删除文章封面")
    @Parameter(name = "articleCover", description = "文章封面")
    @LogAnnotation(module = "发布错误", operation = LogConst.DELETE)
    @AccessLimit(seconds = 60, maxCount = 30)
    @GetMapping("/delete/articleCover")
    public ResponseResult<Void> deleteArticleCover(@RequestParam("articleCoverUrl") String articleCoverUrl) {
        return articleService.deleteArticleCover(articleCoverUrl);
    }

    @PreAuthorize("hasAnyAuthority('bird:publish:article')")
    @Operation(summary = "删除3D模型")
    @Parameter(name = "articleCover", description = "3D模型")
    @LogAnnotation(module = "发布错误", operation = LogConst.DELETE)
    @AccessLimit(seconds = 60, maxCount = 30)
    @GetMapping("/delete/threeDImage")
    public ResponseResult<Void> deleteThreeDModel(@RequestParam("threeDUrl") String threeDUrl) {
        return threeDService.deleteThreeDImage(threeDUrl);
    }

    @PreAuthorize("hasAnyAuthority('bird:publish:article')")
    @Operation(summary = "上传文章图片")
    @Parameters({
            @Parameter(name = "articleImage", description = "文章图片"),
            @Parameter(name = "articleId", description = "文章id", required = true)
    })
    @LogAnnotation(module = "文章管理", operation = LogConst.UPLOAD_IMAGE)
    @AccessLimit(seconds = 60, maxCount = 30)
    @PostMapping("/upload/articleImage")
    public ResponseResult<String> uploadArticleImage(
            @RequestParam("articleImage") MultipartFile articleImage
    ) {
        return articleService.uploadArticleImage(articleImage);
    }

//    @PreAuthorize("hasAnyAuthority('bird:article:list')")
    @Operation(summary = "后台获取所有的文章列表")
    @LogAnnotation(module = "文章管理", operation = LogConst.GET)
    @AccessLimit(seconds = 60, maxCount = 30)
    @GetMapping("/back/list")
    public ResponseResult<List<ArticleListVO>> listArticle() {
        return ControllerUtils.messageHandler(() -> articleService.listArticle());
    }

//    @PreAuthorize("hasAnyAuthority('bird:article:search')")
    @Operation(summary = "后端搜索文章列表")
    @Parameters({
            @Parameter(name = "searchArticleDTO", description = "搜索文章信息", required = true)
    })
    @LogAnnotation(module = "文章管理", operation = LogConst.SEARCH)
    @AccessLimit(seconds = 60, maxCount = 30)
    @PostMapping("/back/search")
    public ResponseResult<List<ArticleListVO>> searchArticle(@RequestBody SearchArticleDTO searchArticleDTO) {
        return ControllerUtils.messageHandler(() -> articleService.searchArticle(searchArticleDTO));
    }

    // {"code":200,"msg":"success","data":[{"id":41,"articleTitle":"非洲鸵鸟","articleContent":"## <span style='color:#f47466'>测试</span>文件","isDeleted":null,"status":null},{"id":42,"articleTitle":"test","articleContent":"## 还是一篇<span style='color:#f47466'>测试</span>文章","isDeleted":null,"status":null}]}
//    @PreAuthorize("hasAnyAuthority('bird:article:search')")
    @Operation(summary = "前端搜索文章列表")
    @Parameters({
            @Parameter(name = "keyword", description = "搜素关键词", required = true)
    })
    @AccessLimit(seconds = 60, maxCount = 60)
    @GetMapping("/search")
    public ResponseResult<List<ArticleSearchVO>> listArticlesBySearch( String keyword) {
        return ControllerUtils.messageHandler(() -> articleService.listArticlesBySearch(keyword));
    }


    @PreAuthorize("hasAnyAuthority('bird:article:update')")
    @Operation(summary = "修改文章状态")
    @Parameters({
            @Parameter(name = "id", description = "文章id", required = true),
            @Parameter(name = "status", description = "状态", required = true)
    })
    @LogAnnotation(module = "文章管理", operation = LogConst.UPDATE)
    @AccessLimit(seconds = 60, maxCount = 30)
    @PostMapping("/back/update/status")
    public ResponseResult<Void> updateArticleStatus(
            @RequestParam("id") @NotNull Long id,
            @RequestParam("status") @NotNull Integer status
    ) {
        return articleService.updateStatus(id, status);
    }

    @PreAuthorize("hasAnyAuthority('bird:article:update')")
    @Operation(summary = "修改文章是否顶置")
    @Parameters({
            @Parameter(name = "id", description = "文章id", required = true),
            @Parameter(name = "isTop", description = "是否顶置", required = true)
    })
    @LogAnnotation(module = "文章管理", operation = LogConst.UPDATE)
    @AccessLimit(seconds = 60, maxCount = 30)
    @PostMapping("/back/update/isTop")
    public ResponseResult<Void> updateArticleIsTop(
            @RequestParam("id") @NotNull Long id,
            @RequestParam("isTop") @NotNull Integer isTop
    ) {
        return articleService.updateIsTop(id, isTop);
    }

    @PreAuthorize("hasAnyAuthority('bird:article:echo')")
    @Operation(summary = "回显文章数据")
    @Parameters({
            @Parameter(name = "id", description = "文章id", required = true)
    })
    @LogAnnotation(module = "文章管理", operation = LogConst.GET)
    @AccessLimit(seconds = 60, maxCount = 30)
    @GetMapping("/back/echo/{id}")
    public ResponseResult<ArticleDTO> getArticleEcho(@PathVariable("id") Long id){
        return ControllerUtils.messageHandler(() -> articleService.getArticleDTO(id));
    }

    @PreAuthorize("hasAnyAuthority('bird:article:delete')")
    @Operation(summary = "删除文章")
    @Parameters({
            @Parameter(name = "id", description = "文章id", required = true)
    })
    @LogAnnotation(module = "文章管理", operation = LogConst.DELETE)
    @AccessLimit(seconds = 60, maxCount = 30)
    @DeleteMapping("/back/delete")
    public ResponseResult<Void> deleteArticle(@RequestBody List<Long> ids) {
        return articleService.deleteArticle(ids);
    }
}
