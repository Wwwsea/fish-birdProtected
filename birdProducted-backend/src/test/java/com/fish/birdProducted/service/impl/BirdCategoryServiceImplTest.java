package com.fish.birdProducted.service.impl;

import com.fish.birdProducted.domain.dto.BirdCategoryDTO;
import com.fish.birdProducted.domain.dto.CategoryDTO;
import com.fish.birdProducted.domain.dto.SearchBirdCategoryDTO;
import com.fish.birdProducted.domain.vo.BirdCategoryVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryName("testCate");
        categoryDTO.setId(11L);
//        cc.addCategory(categoryDTO);
    }

    @Test
    void searchBirdCategory() {
        SearchBirdCategoryDTO searchDto = new SearchBirdCategoryDTO();
        searchDto.setCategoryName("testCate");
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
        birdCategoryDTO.setId(19L);
        birdCategoryDTO.setCategoryName("tttestCate01");
        birdCategoryDTO.setBiologyBranch("tttestBranch01");
        birdCategoryDTO.setParentId(18);
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