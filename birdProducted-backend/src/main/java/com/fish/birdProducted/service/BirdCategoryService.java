package com.fish.birdProducted.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fish.birdProducted.domain.dto.BirdCategoryDTO;
import com.fish.birdProducted.domain.dto.SearchBirdCategoryDTO;
import com.fish.birdProducted.domain.entity.BirdCategory;
import com.fish.birdProducted.domain.response.ResponseResult;
import com.fish.birdProducted.domain.vo.BirdCategoryVO;

import java.util.List;

/**
 * @author:fish
 * @date: 2024/3/15-14:53
 * @content:
 */
public interface BirdCategoryService extends IService<BirdCategory> {
    /**
     * 查询所有分类
     *
     * @return vo
     */
//    List<BirdCategoryVO> listAllCategory();
    List<BirdCategoryVO> listAllCategory();

    List<BirdCategory> listCategoryListNoTime();

    /**
     * 添加分类
     * @param birdCategoryDTO 分类
     * @return 是否成功
     */
    ResponseResult<Void> addBirdCategory(BirdCategoryDTO birdCategoryDTO);

    /**
     * 搜索分类
     * @param searchBirdCategoryDTO 搜索标签DTO
     * @return 分类列表
     */
    List<BirdCategoryVO> searchBirdCategory(SearchBirdCategoryDTO searchBirdCategoryDTO);

    /**
     * 根据id查询
     * @param id id
     * @return 标签
     */
    BirdCategoryVO getBirdCategoryById(Long id);

    /**
     * 新增或修改标签
     * @param birdCategoryDTO 标签DTO
     * @return 是否成功
     */
    ResponseResult<Void> addOrUpdateBirdCategory(BirdCategoryDTO birdCategoryDTO);

    /**
     * 根据id删除
     * @param id id
     * @return 是否成功
     */
    ResponseResult<Void> deleteBirdCategoryByIds(Long id);

}
