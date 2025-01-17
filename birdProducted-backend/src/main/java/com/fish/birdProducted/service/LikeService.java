package com.fish.birdProducted.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fish.birdProducted.domain.entity.Like;
import com.fish.birdProducted.domain.response.ResponseResult;

import java.util.List;


/**
 * (Like)表服务接口
 *
 * @author fish
 * @since 2024-2-18 19:41:19
 */
public interface LikeService extends IService<Like> {

    /**
     * 点赞文章
     *
     * @param type   点赞类型
     * @param typeId 点赞id
     * @return 点赞结果
     */
    ResponseResult<Void> userLike(Integer type, Integer typeId);

    /**
     * 取消点赞
     *
     * @param type   点赞类型
     * @param typeId 点赞id
     * @return 取消点赞结果
     */
    ResponseResult<Void> cancelLike(Integer type, Integer typeId);

    /**
     * 是否点赞
     *
     * @param type   点赞类型
     * @param typeId 点赞id
     * @return 是否点赞
     */
    ResponseResult<List<Like>> isLike(Integer type, Integer typeId);

    /**
     * 获取点赞数
     *
     * @param likeTypeComment 点赞类型
     * @param id              点赞id
     * @return 点赞数量
     */
    Long getLikeCount(Integer likeTypeComment, Long id);
}
