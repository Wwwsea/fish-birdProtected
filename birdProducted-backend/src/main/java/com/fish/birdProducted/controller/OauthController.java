package com.fish.birdProducted.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.request.AuthGiteeRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fish.birdProducted.annotation.AccessLimit;
import com.fish.birdProducted.domain.request.oauth.GiteeBody;
import com.fish.birdProducted.enums.RegisterTypeEnum;
import com.fish.birdProducted.service.OauthService;

import java.io.IOException;

/**
 * @author fish
 * <p>
 * 创建时间：2024/2/21 14:52
 */
@Slf4j
@Tag(name = "第三方登录")
@RestController
@RequestMapping("/oauth")
public class OauthController {

    @Resource
    private GiteeBody giteeBody;


    @Resource
    private OauthService oauthService;

    @Value("${web.index.path}")
    private String path;

    // gitee登录
    @Operation(summary = "Gitee登录")
    @AccessLimit(seconds = 60, maxCount = 5)
    @GetMapping("/gitee/render")
    public void giteeRenderAuth(HttpServletResponse response) throws IOException {
        AuthRequest authRequest = getGiteeAuthRequest();
        response.sendRedirect(authRequest.authorize(AuthStateUtils.createState()));
    }

    @Operation(summary = "Gitee登录回调")
    @AccessLimit(seconds = 60, maxCount = 5)
    @GetMapping("/gitee/callback")
    public void giteeLogin(AuthCallback callback, HttpServletRequest request,
                           HttpServletResponse response) throws IOException {
        AuthRequest authRequest = getGiteeAuthRequest();
        String parameter = oauthService.handleLogin(authRequest.login(callback),
                            request, RegisterTypeEnum.GITEE.getRegisterType());
        response.sendRedirect(path+parameter);
    }

    /**
     * 获取gitee授权请求
     * @return gitee授权请求
     */
    private AuthRequest getGiteeAuthRequest() {
        return new AuthGiteeRequest(AuthConfig.builder()
                .clientId(giteeBody.getClientId())
                .clientSecret(giteeBody.getClientSecret())
                .redirectUri(giteeBody.getRedirectUri())
                .build());
    }
}
