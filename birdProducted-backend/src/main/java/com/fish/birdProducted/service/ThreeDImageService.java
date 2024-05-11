package com.fish.birdProducted.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fish.birdProducted.domain.entity.ThreeDImage;
import com.fish.birdProducted.domain.response.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author:fish
 * @date: 2024/3/29-17:29
 * @content:
 */
public interface ThreeDImageService extends IService<ThreeDImage> {
    // 上传
    ResponseResult<String> uploadThreeDImage(MultipartFile threeDImage);

    // 删除
    ResponseResult<Void> deleteThreeDImage(String threeDImageUrl);



    }
