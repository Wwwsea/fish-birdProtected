package com.fish.birdProducted.controller;

import com.fish.birdProducted.domain.dto.ChatDTO;
import com.fish.birdProducted.domain.dto.ChatGptIsCheckDTO;
import com.fish.birdProducted.domain.dto.SearchChatGptDTO;
import com.fish.birdProducted.domain.response.ResponseResult;
import com.fish.birdProducted.domain.vo.ChatGptListVO;
import com.fish.birdProducted.domain.vo.ChatSessionDetailVO;
import com.fish.birdProducted.domain.vo.ChatSessionVO;
import com.fish.birdProducted.service.ChatGptService;
import com.fish.birdProducted.utils.ControllerUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import com.fish.birdProducted.annotation.AccessLimit;
import com.fish.birdProducted.annotation.LogAnnotation;
import com.fish.birdProducted.constants.LogConst;

import java.util.List;

/**
 * (ChatGpt)表控制层
 *
 * @author fish
 * @since 2024-3-07 17:13:41
 */
@RestController
@Tag(name = "Gpt聊天相关接口")
@RequestMapping("chatGpt")
@Validated
public class ChatGptController {

    @Resource
    private ChatGptService chatGptService;

    @Operation(summary = "聊天")
    @AccessLimit(seconds = 60, maxCount = 10)
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/openai/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter chat(@RequestBody @Valid ChatDTO chatDTO) {
        return chatGptService.sendGptRequest(chatDTO);
    }

    @Operation(summary = "保存会话")
    @AccessLimit(seconds = 60, maxCount = 10)
    @PostMapping(value = "/auth/saveConversation")
    public ResponseResult<String> saveConversation(@RequestBody @Valid ChatDTO chatDTO) {
        return chatGptService.saveChat(chatDTO);
    }

    @Operation(summary = "获取会话列表")
    @AccessLimit(seconds = 60, maxCount = 60)
    @GetMapping(value = "/auth/conversation/list")
    public ResponseResult<List<ChatSessionVO>> getConversationList() {
        return ControllerUtils.messageHandler(() -> chatGptService.queryChatList());
    }

    @Operation(summary = "获取会话详情")
    @Parameter(name = "id", description = "会话id", required = true)
    @AccessLimit(seconds = 60, maxCount = 60)
    @GetMapping(value = "/auth/conversation/detail")
    public ResponseResult<ChatSessionDetailVO> getConversationDetail(@NotNull Long id) {
        return ControllerUtils.messageHandler(() -> chatGptService.queryChatDetail(id));
    }

    @Operation(summary = "删除会话")
    @Parameter(name = "id", description = "会话id", required = true)
    @AccessLimit(seconds = 60, maxCount = 60)
    @DeleteMapping(value = "/auth/conversation/delete")
    public ResponseResult<Void> deleteConversation(@NotNull Long id) {
        return chatGptService.deleteChat(id);
    }

    @PreAuthorize("hasAnyAuthority('bird:chatGpt:list')")
    @Operation(summary = "后台聊天列表")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module="聊天管理",operation= LogConst.GET)
    @GetMapping("/back/list")
    public ResponseResult<List<ChatGptListVO>> backList() {
        return ControllerUtils.messageHandler(() -> chatGptService.getBackChatGptList(null));
    }

    @PreAuthorize("hasAnyAuthority('bird:chatGpt:search')")
    @Operation(summary = "搜索后台聊天列表")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module="聊天管理",operation= LogConst.SEARCH)
    @PostMapping("/back/search")
    public ResponseResult<List<ChatGptListVO>> backList(@RequestBody SearchChatGptDTO searchDTO) {
        return ControllerUtils.messageHandler(() -> chatGptService.getBackChatGptList(searchDTO));
    }

    @PreAuthorize("hasAnyAuthority('bird:chatGpt:isCheck')")
    @Operation(summary = "修改聊天是否通过")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module="聊天管理",operation= LogConst.UPDATE)
    @PostMapping("/back/isCheck")
    public ResponseResult<Void> isCheck(@RequestBody @Valid ChatGptIsCheckDTO chatGptIsCheckDTO) {
        return chatGptService.isCheckChatGpt(chatGptIsCheckDTO);
    }

    @PreAuthorize("hasAnyAuthority('bird:chatGpt:delete')")
    @Operation(summary = "删除聊天")
    @AccessLimit(seconds = 60, maxCount = 30)
    @LogAnnotation(module="聊天管理",operation= LogConst.DELETE)
    @DeleteMapping("/back/delete")
    public ResponseResult<Void> delete(@RequestBody List<Long> ids) {
        return chatGptService.deleteChatGpt(ids);
    }
}