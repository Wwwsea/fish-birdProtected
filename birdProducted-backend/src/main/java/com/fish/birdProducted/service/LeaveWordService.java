package com.fish.birdProducted.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fish.birdProducted.domain.dto.LeaveWordIsCheckDTO;
import com.fish.birdProducted.domain.dto.SearchLeaveWordDTO;
import com.fish.birdProducted.domain.entity.LeaveWord;
import com.fish.birdProducted.domain.response.ResponseResult;
import com.fish.birdProducted.domain.vo.LeaveWordListVO;
import com.fish.birdProducted.domain.vo.LeaveWordVO;

import java.util.List;


/**
 * (LeaveWord)表服务接口
 *
 * @author fish
 * @since 2024-3-03 15:01:11
 */
public interface LeaveWordService extends IService<LeaveWord> {

    /**
     * 查询留言板
     * @return 留言板列表
     */
    List<LeaveWordVO> getLeaveWordList(String id);

    /**
     * 添加留言板
     *
     * @param content 留言内容
     * @return 是否成功
     */
    ResponseResult<Void> userLeaveWord(String content);

    /**
     * 后台留言列表
     * @return 结果
     */
    List<LeaveWordListVO> getBackLeaveWordList(SearchLeaveWordDTO searchDTO);

    /**
     * 是否通过留言
     * @param isCheckDTO 是否通过
     * @return 是否成功
     */
    ResponseResult<Void> isCheckLeaveWord(LeaveWordIsCheckDTO isCheckDTO);

    /**
     * 删除留言
     * @param ids id 列表
     * @return 是否成功
     */
    ResponseResult<Void> deleteLeaveWord(List<Long> ids);
}
