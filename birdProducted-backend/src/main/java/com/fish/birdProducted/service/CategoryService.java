package com.fish.birdProducted.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fish.birdProducted.domain.dto.CategoryDTO;
import com.fish.birdProducted.domain.dto.SearchCategoryDTO;
import com.fish.birdProducted.domain.entity.Category;
import com.fish.birdProducted.domain.response.ResponseResult;
import com.fish.birdProducted.domain.vo.CategoryVO;

import java.util.List;


/**
 * (Category)表服务接口
 *
 * @author fish
 * @since 2023-10-15 02:29:13
 */
public interface CategoryService extends IService<Category> {
    /**
     * 查询所有分类
     *
     * @return vo
     */
    List<CategoryVO> listAllCategory();

    /**
     * 添加分类
     * @param categoryDTO 分类
     * @return 是否成功
     */
    ResponseResult<Void> addCategory(CategoryDTO categoryDTO);

    /**
     * 搜索分类
     * @param searchCategoryDTO 搜索标签DTO
     * @return 分类列表
     */
    List<CategoryVO> searchCategory(SearchCategoryDTO searchCategoryDTO);

    /**
     * 根据id查询
     * @param id id
     * @return 标签
     */
    CategoryVO getCategoryById(Long id);

    /**
     * 新增或修改标签
     * @param categoryDTO 标签DTO
     * @return 是否成功
     */
    ResponseResult<Void> addOrUpdateCategory(CategoryDTO categoryDTO);

    /**
     * 根据id删除
     * @param ids id
     * @return 是否成功
     */
    ResponseResult<Void> deleteCategoryByIds(List<Long> ids);

}
