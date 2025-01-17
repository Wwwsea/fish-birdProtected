package com.fish.birdProducted.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fish.birdProducted.constants.RedisConst;
import com.fish.birdProducted.domain.entity.Like;
import com.fish.birdProducted.domain.response.ResponseResult;
import com.fish.birdProducted.enums.LikeEnum;
import com.fish.birdProducted.mapper.LikeMapper;
import com.fish.birdProducted.service.LikeService;
import com.fish.birdProducted.utils.RedisCache;
import com.fish.birdProducted.utils.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * (Like)表服务实现类
 *
 * @author fish
 * @since 2024-2-18 19:41:19
 */
@Service("likeService")
public class LikeServiceImpl extends ServiceImpl<LikeMapper, Like> implements LikeService {

    @Resource
    private LikeMapper likeMapper;

    @Resource
    private RedisCache redisCache;

    @Override
    public ResponseResult<Void> userLike(Integer type, Integer typeId) {
        // 查询是否已经点赞
        Like like = likeMapper.selectOne(new LambdaQueryWrapper<Like>()
                .eq(Like::getUserId, SecurityUtils.getUserId())
                .eq(Like::getType, type)
                .eq(Like::getTypeId, typeId));
        if (like != null) return ResponseResult.failure();
        Like saveLike = Like.builder()
                .userId(SecurityUtils.getUserId())
                .type(type)
                .typeId(typeId).build();
        if (Objects.equals(type, LikeEnum.LIKE_TYPE_ARTICLE.getType()))
            redisCache.incrementCacheMapValue(RedisConst.ARTICLE_LIKE_COUNT, typeId.toString(), 1);
        if (this.save(saveLike)) return ResponseResult.success();
        return ResponseResult.failure();
    }

    @Override
    public ResponseResult<Void> cancelLike(Integer type, Integer typeId) {
        // 查询是否已经点赞
        Like isLike = likeMapper.selectOne(new LambdaQueryWrapper<Like>()
                .eq(Like::getUserId, SecurityUtils.getUserId())
                .eq(Like::getType, type)
                .eq(Like::getTypeId, typeId));
        if (Objects.isNull(isLike)) return ResponseResult.failure();
        boolean like = this.remove(new LambdaQueryWrapper<Like>()
                .eq(Like::getUserId, SecurityUtils.getUserId())
                .eq(Like::getType, type)
                .eq(Like::getTypeId, typeId));
        if (Objects.equals(type, LikeEnum.LIKE_TYPE_ARTICLE.getType()))
            redisCache.incrementCacheMapValue(RedisConst.ARTICLE_LIKE_COUNT, typeId.toString(), -1);
        if (like) return ResponseResult.success();
        return ResponseResult.failure();
    }

    @Override
    public ResponseResult<List<Like>> isLike(Integer type, Integer typeId) {
        if (SecurityUtils.isLogin()) {
            LambdaQueryWrapper<Like> wrapper = new LambdaQueryWrapper<>();
            if (Objects.equals(type, LikeEnum.LIKE_TYPE_ARTICLE.getType())) {
                List<Like> like = likeMapper.selectList(wrapper
                        .eq(Like::getUserId, SecurityUtils.getUserId())
                        .eq(Like::getType, type)
                        .eq(Like::getTypeId, typeId));
                if (!like.isEmpty()) return ResponseResult.success(like);
                else ResponseResult.failure(null);
            }
            if (Objects.equals(type, LikeEnum.LIKE_TYPE_COMMENT.getType()) || Objects.equals(type, LikeEnum.LIKE_TYPE_LEAVE_WORD.getType())) {
                if (Objects.equals(type, LikeEnum.LIKE_TYPE_LEAVE_WORD.getType())) wrapper.eq(Like::getTypeId, typeId);
                List<Like> like = likeMapper.selectList(wrapper
                        .eq(Like::getUserId, SecurityUtils.getUserId())
                        .eq(Like::getType, type));
                if (!like.isEmpty()) return ResponseResult.success(like);
                else ResponseResult.failure(null);
            }

        }
        return ResponseResult.failure(null);
    }

    @Override
    public Long getLikeCount(Integer likeTypeComment, Long id) {
        return likeMapper.selectCount(new LambdaQueryWrapper<Like>()
                .eq(Like::getType, likeTypeComment)
                .eq(Like::getTypeId, id));
    }

}
