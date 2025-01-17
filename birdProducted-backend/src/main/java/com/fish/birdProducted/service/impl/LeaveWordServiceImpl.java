package com.fish.birdProducted.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fish.birdProducted.domain.entity.*;
import com.fish.birdProducted.mapper.*;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.fish.birdProducted.constants.FunctionConst;
import com.fish.birdProducted.constants.SQLConst;
import com.fish.birdProducted.domain.dto.LeaveWordIsCheckDTO;
import com.fish.birdProducted.domain.dto.SearchLeaveWordDTO;
import com.fish.birdProducted.domain.response.ResponseResult;
import com.fish.birdProducted.domain.vo.LeaveWordListVO;
import com.fish.birdProducted.domain.vo.LeaveWordVO;
import com.fish.birdProducted.enums.CommentEnum;
import com.fish.birdProducted.enums.LikeEnum;
import com.fish.birdProducted.service.LeaveWordService;
import com.fish.birdProducted.utils.SecurityUtils;
import com.fish.birdProducted.utils.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * (LeaveWord)表服务实现类
 *
 * @author fish
 * @since 2024-3-03 15:01:11
 */
@Service("leaveWordService")
public class LeaveWordServiceImpl extends ServiceImpl<LeaveWordMapper, LeaveWord> implements LeaveWordService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private LikeMapper likeMapper;

    @Resource
    private FavoriteMapper favoriteMapper;

    @Resource
    private LeaveWordMapper leaveWordMapper;

    @Override
    public List<LeaveWordVO> getLeaveWordList(String id) {
        return this.query()
                .eq(SQLConst.IS_CHECK, SQLConst.IS_CHECK_YES)
                .eq(id != null, SQLConst.ID, id)
                .orderByDesc(SQLConst.CREATE_TIME)
                .list().stream().map(leaveWord -> {
                    User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, leaveWord.getUserId()));
                    return leaveWord.asViewObject(LeaveWordVO.class, leaveWordVO -> leaveWordVO.setNickname(user.getNickname())
                            .setAvatar(user.getAvatar())
                            .setCommentCount(commentMapper.selectCount(new LambdaQueryWrapper<Comment>().eq(Comment::getType, CommentEnum.COMMENT_TYPE_LEAVE_WORD.getType()).eq(Comment::getIsCheck, SQLConst.IS_CHECK_YES).eq(Comment::getTypeId, leaveWord.getId())))
                            .setLikeCount(likeMapper.selectCount(new LambdaQueryWrapper<Like>().eq(Like::getType, LikeEnum.LIKE_TYPE_LEAVE_WORD.getType()).eq(Like::getTypeId, leaveWord.getId())))
                            .setFavoriteCount(favoriteMapper.selectCount(new LambdaQueryWrapper<Favorite>().eq(Favorite::getType, CommentEnum.COMMENT_TYPE_LEAVE_WORD.getType()).eq(Favorite::getTypeId, leaveWord.getId()))));
                }).toList();
    }

    @Override
    public ResponseResult<Void> userLeaveWord(String content) {
        String parse = (String) JSON.parse(content);
        if (parse.length() > FunctionConst.LEAVE_WORD_CONTENT_LENGTH) {
            return ResponseResult.failure("留言内容过长");
        }
        LeaveWord build = LeaveWord.builder().content(parse)
                .userId(SecurityUtils.getUserId()).build();
        return this.save(build) ? ResponseResult.success() : ResponseResult.failure();
    }

    @Override
    public List<LeaveWordListVO> getBackLeaveWordList(SearchLeaveWordDTO searchDTO) {
        LambdaQueryWrapper<LeaveWord> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotNull(searchDTO)) {
            // 搜索
            List<User> users = userMapper.selectList(new LambdaQueryWrapper<User>().like(User::getUsername, searchDTO.getUserName()));
            if (!users.isEmpty())
                wrapper.in(StringUtils.isNotEmpty(searchDTO.getUserName()), LeaveWord::getUserId, users.stream().map(User::getId).collect(Collectors.toList()));
            else
                wrapper.eq(StringUtils.isNotNull(searchDTO.getUserName()), LeaveWord::getUserId, null);

            wrapper.eq(StringUtils.isNotNull(searchDTO.getIsCheck()), LeaveWord::getIsCheck, searchDTO.getIsCheck());
            if (StringUtils.isNotNull(searchDTO.getStartTime()) && StringUtils.isNotNull(searchDTO.getEndTime()))
                wrapper.between(LeaveWord::getCreateTime, searchDTO.getStartTime(), searchDTO.getEndTime());
        }
        List<LeaveWord> leaveWords = leaveWordMapper.selectList(wrapper);
        if (!leaveWords.isEmpty()) {
            return leaveWords.stream().map(leaveWord -> leaveWord.asViewObject(LeaveWordListVO.class,
                    v -> v.setUserName(userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, leaveWord.getUserId()))
                            .getUsername()))).toList();
        }
        return null;
    }

    @Override
    public ResponseResult<Void> isCheckLeaveWord(LeaveWordIsCheckDTO isCheckDTO) {
        if (leaveWordMapper.updateById(LeaveWord.builder().id(isCheckDTO.getId()).isCheck(isCheckDTO.getIsCheck()).build()) > 0)
            return ResponseResult.success();

        return ResponseResult.failure();
    }

    @Override
    public ResponseResult<Void> deleteLeaveWord(List<Long> ids) {
        if (leaveWordMapper.deleteBatchIds(ids) > 0) {
            return ResponseResult.success();
        }
        return ResponseResult.failure();
    }
}
