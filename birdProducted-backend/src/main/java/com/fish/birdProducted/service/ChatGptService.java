package com.fish.birdProducted.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import com.fish.birdProducted.domain.dto.ChatDTO;
import com.fish.birdProducted.domain.dto.ChatGptIsCheckDTO;
import com.fish.birdProducted.domain.dto.SearchChatGptDTO;
import com.fish.birdProducted.domain.entity.ChatGpt;
import com.fish.birdProducted.domain.response.ResponseResult;
import com.fish.birdProducted.domain.vo.ChatGptListVO;
import com.fish.birdProducted.domain.vo.ChatSessionDetailVO;
import com.fish.birdProducted.domain.vo.ChatSessionVO;

import java.util.List;


/**
 * (ChatGpt)表服务接口
 *
 * @author fish
 * @since 2024-3-07 17:13:42
 */
public interface ChatGptService extends IService<ChatGpt> {
    /**
     * 发送gpt请求
     * @return SseEmitter
     */
    SseEmitter sendGptRequest(ChatDTO chatDTO);

    /**
     * 保存会话
     * @param chatDTO 会话信息
     * @return ResponseResult<String>
     */
    ResponseResult<String> saveChat(ChatDTO chatDTO);

    /**
     * 查询会话列表
     * @return List<ChatSessionVO>
     */
    List<ChatSessionVO> queryChatList();

    /**
     * 回到会话
     * @param Long 会话id
     * @return ChatSessionDetailVO
     */
    ChatSessionDetailVO queryChatDetail(Long Long);

    /**
     * 删除会话
     */
    ResponseResult<Void> deleteChat(Long id);

    /**
     * 后台会话列表
     * @return 结果
     */
    List<ChatGptListVO> getBackChatGptList(SearchChatGptDTO searchDTO);

    /**
     * 是否通过会话
     * @param isCheckDTO 是否通过
     * @return 是否成功
     */
    ResponseResult<Void> isCheckChatGpt(ChatGptIsCheckDTO isCheckDTO);

    /**
     * 删除会话
     * @param ids id 列表
     * @return 是否成功
     */
    ResponseResult<Void> deleteChatGpt(List<Long> ids);
}
