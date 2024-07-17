package com.fish.birdProducted.service.impl;

import com.fish.birdProducted.constants.RedisConst;
import com.fish.birdProducted.mapper.ArticleMapper;
import com.fish.birdProducted.service.RedisService;
import com.fish.birdProducted.utils.RedisCache;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author fish
 * <p>
 * 创建时间：2024/10/22 15:18
 */
@Slf4j
@Service
public class RedisServiceImpl implements RedisService {

    @Resource
    private RedisCache redisCache;

    @Resource
    private ArticleMapper articleMapper;

    @Override
    public void articleCountClear() {
        log.info("--------执行清除redis文章相关数量缓存--------");
        boolean isDel = redisCache.deleteObject(RedisConst.ARTICLE_FAVORITE_COUNT);
        isDel = isDel && redisCache.deleteObject(RedisConst.ARTICLE_LIKE_COUNT);
        isDel = isDel && redisCache.deleteObject(RedisConst.ARTICLE_COMMENT_COUNT);
        if (isDel) {
            log.info("--------清除redis文章相关数量缓存成功--------");
        }
        log.info("--------清除redis文章相关数量缓存失败--------");
    }

    @Override
    public void articleVisitCount() {
        try {
            articleMapper.selectList(null).forEach(article -> redisCache.setCacheObject(RedisConst.ARTICLE_VISIT_COUNT + article.getId(), article.getVisitCount()));
            log.info("--------执行redis文章访问量缓存成功--------");
        } catch (Exception e) {
            log.error("--------执行redis文章访问量缓存失败",e);
        }
    }
}
