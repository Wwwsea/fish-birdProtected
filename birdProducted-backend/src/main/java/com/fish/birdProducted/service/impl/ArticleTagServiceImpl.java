package com.fish.birdProducted.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fish.birdProducted.domain.entity.ArticleTag;
import com.fish.birdProducted.mapper.ArticleTagMapper;
import com.fish.birdProducted.service.ArticleTagService;
import org.springframework.stereotype.Service;

/**
 * (ArticleTag)表服务实现类
 *
 * @author fish
 * @since 2024-10-15 02:29:13
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

}
