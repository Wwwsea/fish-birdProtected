package com.fish.birdProducted.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fish.birdProducted.domain.dto.LinkDTO;
import com.fish.birdProducted.domain.dto.LinkIsCheckDTO;
import com.fish.birdProducted.domain.dto.SearchLinkDTO;
import com.fish.birdProducted.domain.entity.Link;
import com.fish.birdProducted.domain.response.ResponseResult;
import com.fish.birdProducted.domain.vo.LinkListVO;
import com.fish.birdProducted.domain.vo.LinkVO;

import java.util.List;


/**
 * (Link)表服务接口
 *
 * @author fish
 * @since 2024-3-14 08:43:35
 */
public interface LinkService extends IService<Link> {

    /**
     * 申请友链
     * @param linkDTO 友链信息
     * @return 是否成功
     */
    ResponseResult<Void> applyLink(LinkDTO linkDTO);

    /**
     * 查询通过审核的友链
     */
    List<LinkVO> getLinkList();


    /**
     * 后台友链列表
     * @return 结果
     */
    List<LinkListVO> getBackLinkList(SearchLinkDTO searchDTO);

    /**
     * 是否通过友链
     * @param isCheckDTO 是否通过
     * @return 是否成功
     */
    ResponseResult<Void> isCheckLink(LinkIsCheckDTO isCheckDTO);

    /**
     * 删除友链
     * @param ids id 列表
     * @return 是否成功
     */
    ResponseResult<Void> deleteLink(List<Long> ids);
}
