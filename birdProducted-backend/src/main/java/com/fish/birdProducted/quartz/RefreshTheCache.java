package com.fish.birdProducted.quartz;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fish.birdProducted.domain.entity.Category;
import com.fish.birdProducted.domain.entity.Tag;
import com.fish.birdProducted.domain.vo.ArticleListVO;
import com.fish.birdProducted.domain.vo.ArticleVO;
import com.fish.birdProducted.service.ArticleService;
import jakarta.annotation.Resource;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import com.fish.birdProducted.constants.RedisConst;
import com.fish.birdProducted.domain.entity.Article;
import com.fish.birdProducted.mapper.ArticleMapper;
import com.fish.birdProducted.utils.RedisCache;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @author fish
 * <p>
 * 创建时间：2024/1/1 22:25
 * 刷新缓存任务 / 5分钟刷新一次
 */
@Slf4j
@Component
public class RefreshTheCache extends QuartzJobBean {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private ArticleService articleService;

    @Resource
    private RedisCache redisCache;
    @Override
    protected void executeInternal(@NonNull JobExecutionContext context) {
        log.info("-------------------------------开始同步文章浏览量到数据库-------------------------------");
        try {
            // 获取所有文章id
            List<Long> articleIds = articleMapper.selectList(null).stream().map(Article::getId).toList();
            // 通过id从redis中获取缓存的访问量
            articleIds.forEach(id -> {
                // 把访问量设置到mysql数据库中
                Long cacheObject = Long.valueOf((Integer)redisCache.getCacheObject(RedisConst.ARTICLE_VISIT_COUNT + id));
                // 不会触发自动填充
                articleMapper.update(null,new LambdaUpdateWrapper<Article>().eq(Article::getId,id).set(Article::getVisitCount,cacheObject));
            });


            System.out.println();
            log.info("-------------------------------同步文章浏览量成功-------------------------------");
        } catch (Exception e) {
            log.error("同步文章浏览量失败",e);
        }
    }
}
