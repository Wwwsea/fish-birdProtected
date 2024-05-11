package com.fish.birdProducted.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fish.birdProducted.domain.entity.ThreeDImage;
import com.fish.birdProducted.domain.response.ResponseResult;
import com.fish.birdProducted.enums.UploadEnum;
import com.fish.birdProducted.mapper.ThreeDImageMapper;
import com.fish.birdProducted.service.ThreeDImageService;
import com.fish.birdProducted.utils.FileUploadUtils;
import com.fish.birdProducted.utils.StringUtils;
import com.fish.birdProducted.utils.UnZipFileUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * @author:fish
 * @date: 2024/3/29-17:25
 * @content:
 */
@Service("threeDImageServiceService")
public class ThreeDImageServiceImpl extends ServiceImpl<ThreeDImageMapper, ThreeDImage> implements ThreeDImageService {

    @Resource
    private FileUploadUtils fileUploadUtils;

    @Resource
    private UnZipFileUtils unZipFileUtils;

    @Value("${minio.bucketName}")
    private String bucketName;

    public ResponseResult<String> uploadThreeDImage(MultipartFile threeDImage) {
        try {
            String threeDImageUrl = fileUploadUtils.upload(UploadEnum.IMAGE_3D, threeDImage);
            if (StringUtils.isNotNull(threeDImageUrl)){
                // 解压文件
                String zipFilePath = Objects.requireNonNull(threeDImage.getOriginalFilename()); // 获取上传的zip文件名
                String desDirectory = "C:/Program Files/"; // 设置解压目标路径
                unZipFileUtils.unzip(zipFilePath, desDirectory);

                // 删除解压后的文件
                unZipFileUtils.deleteFile(desDirectory + new File(zipFilePath).getName().replace(".zip", ""));
                return ResponseResult.success(threeDImageUrl);

            }
            else
                return ResponseResult.failure("上传格式错误");
        } catch (Exception e) {
            log.error("文章封面上传失败", e);
            return ResponseResult.failure();
        }
    }



    public ResponseResult<Void> deleteThreeDImage(String threeDImageUrl) {
        try {
            // 提取图片名称
            String threeDImageName = threeDImageUrl.substring(threeDImageUrl.indexOf(bucketName) + bucketName.length());
            fileUploadUtils.deleteFiles(List.of(threeDImageName));
            return ResponseResult.success();
        } catch (Exception e) {
            log.error("删除文章封面失败", e);
            return ResponseResult.failure();
        }
    }



}

