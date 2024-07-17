package com.fish.birdProducted.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fish.birdProducted.domain.dto.StationmasterInfoDTO;
import com.fish.birdProducted.domain.dto.WebsiteInfoDTO;
import com.fish.birdProducted.domain.entity.Article;
import com.fish.birdProducted.domain.response.ResponseResult;
import com.fish.birdProducted.domain.vo.WebsiteInfoVO;
import com.fish.birdProducted.mapper.ArticleMapper;
import com.fish.birdProducted.mapper.BirdCategoryMapper;
import com.fish.birdProducted.mapper.CommentMapper;
import com.fish.birdProducted.mapper.WebsiteInfoMapper;
import com.fish.birdProducted.service.WebsiteInfoService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.fish.birdProducted.constants.SQLConst;
import com.fish.birdProducted.constants.WebsiteInfoConst;
import com.fish.birdProducted.domain.entity.WebsiteInfo;
import com.fish.birdProducted.enums.UploadEnum;
import com.fish.birdProducted.utils.FileUploadUtils;
import com.fish.birdProducted.utils.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * (WebsiteInfo)表服务实现类
 *
 * @author fish
 * @since 2024-2-27 14:07:34
 */
@Slf4j
@Service("websiteInfoService")
public class WebsiteInfoServiceImpl extends ServiceImpl<WebsiteInfoMapper, WebsiteInfo> implements WebsiteInfoService {

    @Resource
    private FileUploadUtils fileUploadUtils;

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private BirdCategoryMapper categoryMapper;

    @Resource
    private CommentMapper commentMapper;

    @Transactional
    @Override
    public ResponseResult<String> uploadImageInsertOrUpdate(UploadEnum uploadEnum, MultipartFile avatar, Integer type) {
        try {
            List<String> avatarNames = fileUploadUtils.listFiles(uploadEnum.getDir());
            if (!avatarNames.isEmpty()) {
                if (fileUploadUtils.deleteFiles(avatarNames))
                    log.info("删除旧图片成功,{}", avatarNames);
            }
            // 上传
            String url = fileUploadUtils.upload(uploadEnum, avatar);
            switch (type) {
                case 0 ->
                        this.saveOrUpdate(WebsiteInfo.builder().webmasterAvatar(url).id(WebsiteInfoConst.WEBSITE_INFO_ID).build());
                case 1 ->
                        this.saveOrUpdate(WebsiteInfo.builder().webmasterProfileBackground(url).id(WebsiteInfoConst.WEBSITE_INFO_ID).build());
            }

            if (StringUtils.isNotNull(url))
                return ResponseResult.success(url);
            else
                return ResponseResult.failure("图片格式不正确");

        } catch (Exception e) {
            log.error("上传图片失败", e);
            return ResponseResult.failure();
        }
    }

    @Override
    public WebsiteInfoVO selectWebsiteInfo() {
        WebsiteInfoVO websiteInfoVO = this.getById(WebsiteInfoConst.WEBSITE_INFO_ID).asViewObject(WebsiteInfoVO.class);
        // 运行时长
        if (StringUtils.isNotNull(websiteInfoVO)) {
            LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
            wrapper.select(Article::getUpdateTime).orderByDesc(Article::getUpdateTime).last(SQLConst.LIMIT_ONE_SQL);
            websiteInfoVO.setLastUpdateTime(articleMapper.selectOne(wrapper).getUpdateTime());
            websiteInfoVO.setArticleCount(articleMapper.selectCount(null));
            List<String> listArticleContent = articleMapper.selectList(null).stream().map(Article::getArticleContent).toList();
            // 合成一个string
            String mergedString = String.join("", listArticleContent);
            websiteInfoVO.setWordCount((long) extractTextFromMarkdown(mergedString).length());
            wrapper.clear();
            wrapper.select(Article::getVisitCount);
            websiteInfoVO.setVisitCount(articleMapper.selectObjs(wrapper).stream().mapToLong(visitCount -> (Long) visitCount).sum());
            websiteInfoVO.setArticleCount(categoryMapper.selectCount(null));
            websiteInfoVO.setCommentCount(commentMapper.selectCount(null));
            return websiteInfoVO;
        }
        return null;
    }

    @Transactional
    @Override
    public ResponseResult<Void> updateStationmasterInfo(StationmasterInfoDTO stationmasterInfoDTO) {
        WebsiteInfo websiteInfo = stationmasterInfoDTO.asViewObject(WebsiteInfo.class, v -> v.setId(WebsiteInfoConst.WEBSITE_INFO_ID));
        if (StringUtils.isNotNull(websiteInfo)) {
            this.saveOrUpdate(websiteInfo);
            return ResponseResult.success();
        }
        return ResponseResult.failure();
    }

    @Transactional
    @Override
    public ResponseResult<Void> updateWebsiteInfo(WebsiteInfoDTO websiteInfoDTO) {
        WebsiteInfo websiteInfo = websiteInfoDTO.asViewObject(WebsiteInfo.class, v -> v.setId(WebsiteInfoConst.WEBSITE_INFO_ID));
        if (StringUtils.isNotNull(websiteInfo)) {
            this.saveOrUpdate(websiteInfo);
            return ResponseResult.success();
        }
        return ResponseResult.failure();
    }

    /**
     * 从Markdown文档中提取文字内容
     *
     * @param markdownContent Markdown文档内容
     * @return 文字内容
     */
    private static String extractTextFromMarkdown(String markdownContent) {
        // 使用正则表达式匹配Markdown文档中的文字内容，并去掉空格
        Pattern pattern = Pattern.compile("[^#>\\*\\[\\]`\\s]+");
        Matcher matcher = pattern.matcher(markdownContent);

        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            result.append(matcher.group()).append("\n");
        }

        return result.toString().trim();
    }
}
