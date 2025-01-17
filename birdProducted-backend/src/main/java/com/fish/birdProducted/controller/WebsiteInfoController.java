package com.fish.birdProducted.controller;


import com.fish.birdProducted.domain.dto.StationmasterInfoDTO;
import com.fish.birdProducted.domain.dto.WebsiteInfoDTO;
import com.fish.birdProducted.domain.response.ResponseResult;
import com.fish.birdProducted.domain.vo.WebsiteInfoVO;
import com.fish.birdProducted.enums.UploadEnum;
import com.fish.birdProducted.service.WebsiteInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fish.birdProducted.annotation.AccessLimit;
import com.fish.birdProducted.annotation.LogAnnotation;
import com.fish.birdProducted.constants.LogConst;

/**
 * (WebsiteInfo)表控制层
 *
 * @author fish
 * @since 2024-2-27 14:07:33
 */
@Tag(name = "网站基本信息")
@RestController
@RequestMapping("websiteInfo")
public class WebsiteInfoController {
    /**
     * 服务对象
     */
    @Resource
    private WebsiteInfoService websiteInfoService;

    /**
     * 上传头像
     * @param avatar 头像
     * @return 访问url
     */
    @PreAuthorize("hasAnyAuthority('bird:update:websiteInfo')")
    @Operation(summary = "上传站长头像")
    @Parameter(name = "avatar", description = "头像")
    @AccessLimit(seconds = 60, maxCount = 5)
    @LogAnnotation(module="信息管理",operation= LogConst.UPLOAD_IMAGE)
    @PostMapping("/upload/avatar")
    public ResponseResult<String> upload(@RequestParam("avatar") MultipartFile avatar) {
        return websiteInfoService.uploadImageInsertOrUpdate(UploadEnum.WEBSITE_INFO_AVATAR, avatar,0);
    }

    /**
     * 资料卡背景上传
     * @param background 背景图
     * @return 访问url
     */
    @PreAuthorize("hasAnyAuthority('bird:update:websiteInfo')")
    @Operation(summary = "上传站长资料卡背景")
    @Parameter(name = "background", description = "资料卡片背景")
    @AccessLimit(seconds = 60, maxCount = 5)
    @LogAnnotation(module="信息管理",operation= LogConst.UPLOAD_IMAGE)
    @PostMapping("/upload/background")
    public ResponseResult<String> uploadBackground(@RequestParam("background") MultipartFile background) {
        return websiteInfoService.uploadImageInsertOrUpdate(UploadEnum.WEBSITE_INFO_BACKGROUND, background, 1);
    }

    /**
     * 查询网站信息
     * @return 网站信息
     */
    @PreAuthorize("hasAnyAuthority('bird:get:websiteInfo')")
    @Operation(summary = "查看网站信息-后端")
    @AccessLimit(seconds = 60, maxCount = 30)
    @GetMapping
    public ResponseResult<WebsiteInfoVO> selectWebsiteInfo() {
        return ResponseResult.success(websiteInfoService.selectWebsiteInfo());
    }

    /**
     * 查询网站信息
     * @return 网站信息
     */
    @Operation(summary = "查看网站信息-前端")
    @AccessLimit(seconds = 60, maxCount = 30)
    @GetMapping("/front")
    public ResponseResult<WebsiteInfoVO> selectWebsiteInfoByFront() {
        return ResponseResult.success(websiteInfoService.selectWebsiteInfo());
    }

    /**
     * 修改站长信息
     * @param stationmasterInfoDTO 站长信息
     * @return 是否成功
     */
    @PreAuthorize("hasAnyAuthority('bird:update:websiteInfo')")
    @Operation(summary = "修改或创建站长信息")
    @Parameter(name = "stationmasterInfoDTO", description = "站长信息")
    @PostMapping("/stationmaster")
    public ResponseResult<Void> updateStationmasterInfo(@RequestBody StationmasterInfoDTO stationmasterInfoDTO) {
        return websiteInfoService.updateStationmasterInfo(stationmasterInfoDTO);
    }

    /**
     * 修改或创建网站信息
     * @param websiteInfoDTO 网站信息
     * @return 是否成功
     */
    @PreAuthorize("hasAnyAuthority('bird:update:websiteInfo')")
    @Operation(summary = "修改或创建网站信息")
    @Parameter(name = "websiteInfoDTO", description = "网站信息")
    @PostMapping("/webInfo")
    public ResponseResult<Void> updateWebsiteInfo(@RequestBody WebsiteInfoDTO websiteInfoDTO) {
        return websiteInfoService.updateWebsiteInfo(websiteInfoDTO);
    }
}

