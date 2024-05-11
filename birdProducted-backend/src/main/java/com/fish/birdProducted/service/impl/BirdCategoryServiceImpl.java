package com.fish.birdProducted.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fish.birdProducted.constants.FunctionConst;
import com.fish.birdProducted.domain.dto.BirdCategoryDTO;
import com.fish.birdProducted.domain.dto.SearchBirdCategoryDTO;
import com.fish.birdProducted.domain.entity.*;
import com.fish.birdProducted.domain.response.ResponseResult;
import com.fish.birdProducted.domain.vo.BirdCategoryVO;
import com.fish.birdProducted.domain.vo.CategoryVO;
import com.fish.birdProducted.enums.RespEnum;
import com.fish.birdProducted.mapper.ArticleMapper;
import com.fish.birdProducted.service.BirdCategoryService;
import com.fish.birdProducted.utils.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fish.birdProducted.mapper.BirdCategoryMapper;

import java.util.List;

/**
 * (Category)表服务实现类
 *
 * @author fish
 * @since 2023-10-15 02:29:14
 */
@Service("birdCategoryService")
public class BirdCategoryServiceImpl extends ServiceImpl<BirdCategoryMapper, BirdCategory> implements BirdCategoryService {

    @Resource
    private ArticleMapper articleMapper;


    @Autowired
    private BirdCategoryMapper categoryMapper;

    public List<BirdCategoryVO> listAllCategory() {
        /*return categoryMapper.selectList(new QueryWrapper<BirdCategory>()
                .ne("is_deleted", 1)
        );// 添加条件：is_deleted 不等于 1*/
        List<BirdCategory> categories = this.query()
                .list();

        return categories.stream().map(category -> category.asViewObject(BirdCategoryVO.class, item -> {
            item.setArticleCount(articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                    .eq(Article::getCategoryId, category.getId())
                    .ne(Article::getIsDeleted, 1) // 添加条件：is_deleted 不等于 1
            ));
        })).toList();
    }

    public List<BirdCategory> listCategoryListNoTime() {
         return categoryMapper.selectList(new QueryWrapper<BirdCategory>()
                .ne("is_deleted", 1)
                .select("id", "category_name", "biology_branch", "parent_id")
        );// 添加条件：is_deleted 不等于 1
    }

    public ResponseResult<Void> addBirdCategory(BirdCategoryDTO birdCategoryDTO) {
        if (this.save(birdCategoryDTO.asViewObject(BirdCategory.class))) return ResponseResult.success();
        return ResponseResult.failure();
    }

    @Override
    public List<BirdCategoryVO> searchBirdCategory(SearchBirdCategoryDTO searchBirdCategoryDTO) {
        LambdaQueryWrapper<BirdCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(searchBirdCategoryDTO.getCategoryName())
                            , BirdCategory::getCategoryName, searchBirdCategoryDTO.getCategoryName());
        if (StringUtils.isNotNull(searchBirdCategoryDTO.getStartTime()) && StringUtils.isNotNull(searchBirdCategoryDTO.getEndTime()))
            queryWrapper.between(BirdCategory::getCreateTime, searchBirdCategoryDTO.getStartTime(), searchBirdCategoryDTO.getEndTime());


        return categoryMapper.selectList(queryWrapper)
                .stream()
                .map(category ->
                        category.asViewObject(BirdCategoryVO.class, item ->
                                item.setArticleCount(articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                                        .eq(Article::getCategoryId, category.getId())))))
                .toList();
    }

    @Override
    public BirdCategoryVO getBirdCategoryById(Long id) {
        return categoryMapper.selectById(id).asViewObject(BirdCategoryVO.class);
    }

    @Override
    public ResponseResult<Void> addOrUpdateBirdCategory(BirdCategoryDTO birdCategoryDTO) {
        if (this.saveOrUpdate(birdCategoryDTO.asViewObject(BirdCategory.class))) return ResponseResult.success();
        return ResponseResult.failure();
    }

    @Override
    public ResponseResult<Void> deleteBirdCategoryByIds(Long id) {
        // 判断是否有未删除的子目录
        if (categoryMapper.selectCount(new LambdaQueryWrapper<BirdCategory>().eq(BirdCategory::getParentId, id)) > 0) {
            return ResponseResult.failure(RespEnum.NO_DELETE_CHILD_MENU.getCode(),RespEnum.NO_DELETE_CHILD_MENU.getMsg());
        }
        // 是否有剩下文章
        Long count = articleMapper.selectCount(new LambdaQueryWrapper<Article>().in(Article::getCategoryId, id));
        if (count > 0) return ResponseResult.failure(FunctionConst.CATEGORY_EXIST_ARTICLE);
        // 执行删除
        if (this.removeById(id)) return ResponseResult.success();
        return ResponseResult.failure();
    }
}
