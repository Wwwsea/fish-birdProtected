package com.fish.birdProducted.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fish.birdProducted.domain.dto.LogDTO;
import com.fish.birdProducted.domain.dto.LogDeleteDTO;
import com.fish.birdProducted.domain.entity.Log;
import com.fish.birdProducted.domain.response.ResponseResult;
import com.fish.birdProducted.domain.vo.PageVO;


/**
 * (Log)表服务接口
 *
 * @author fish
 * @since 2024-2-2 09:12:32
 */
public interface LogService extends IService<Log> {
    /**
     * 搜索/显示操作日志
     *
     * @param LogDTO  查询条件
     * @param aLong
     * @param current
     * @return 数据列表
     */
    PageVO searchLog(LogDTO LogDTO, Long aLong, Long current);

    /**
     *  删除/清空操作日志
     * @param logDeleteDTO id集合
     * @return  响应结果
     */
    ResponseResult<Void> deleteLog(LogDeleteDTO logDeleteDTO);

}
