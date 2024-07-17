package com.fish.birdProducted.service.impl;

import com.fish.birdProducted.domain.dto.BirdCategoryDTO;
import com.fish.birdProducted.domain.dto.SearchBirdCategoryDTO;
import com.fish.birdProducted.domain.vo.BirdCategoryVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:fish
 * @date: 2024/4/21-10:50
 * @content:
 */
@SpringBootTest
class BirdCategoryServiceImplTest {

    @Autowired
    private BirdCategoryServiceImpl categoryService;

    @Test
    void listAllCategory(){
        System.out.println(categoryService.listAllCategory());
    }

    @Test
    void addBirdCategory() {

        BirdCategoryDTO birdCategoryDTO = new BirdCategoryDTO();
        birdCategoryDTO.setId(19L);
        birdCategoryDTO.setCategoryName("tttestCate");
        birdCategoryDTO.setBiologyBranch("tttestBranch");
        birdCategoryDTO.setParentId(18);
        categoryService.addBirdCategory(birdCategoryDTO);
//        cc.addCategory(categoryDTO);
    }

    @Test
    void searchBirdCategory() {
        SearchBirdCategoryDTO searchDto = new SearchBirdCategoryDTO();
        searchDto.setCategoryName("鸵鸟目");
        searchDto.setBiologyBranch("目");
        List<BirdCategoryVO> res = categoryService.searchBirdCategory(searchDto);
        System.out.println("res: "+res);
    }

    @Test
    void getBirdCategoryById() {
        BirdCategoryVO res = categoryService.getBirdCategoryById(17L);
        System.out.println(res.getCategoryName());
    }

    @Test
    void addOrUpdateBirdCategory() {
        BirdCategoryDTO birdCategoryDTO = new BirdCategoryDTO();
        birdCategoryDTO.setCategoryName("3333");
        birdCategoryDTO.setBiologyBranch("333");
        birdCategoryDTO.setParentId(19);
        categoryService.addOrUpdateBirdCategory(birdCategoryDTO);

        listAllCategory();

    }

    @Test
    void deleteBirdCategoryByIds() {
        List<Long> ids = new ArrayList<>();
        ids.add(19L);
        categoryService.deleteBirdCategoryByIds(19L);
        listAllCategory();
    }
}