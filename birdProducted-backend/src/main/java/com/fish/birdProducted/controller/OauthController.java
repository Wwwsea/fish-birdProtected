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
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fish.birdProducted.annotation.AccessLimit;
import com.fish.birdProducted.domain.request.oauth.GiteeBody;
import com.fish.birdProducted.domain.request.oauth.GithubBody;
import com.fish.birdProducted.enums.RegisterTypeEnum;
import com.fish.birdProducted.service.OauthService;

import java.io.IOException;

/**
 * @author fish
 * <p>
 * 创建时间：2023/2/21 14:52
 */
@Slf4j
@Tag(name = "第三方登录")
@RestController
@RequestMapping("/oauth")
public class OauthController {

    @Resource
    private GiteeBody giteeBody;

    @Resource
    private GithubBody githubBody;

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
    // github登录
    @Operation(summary = "Github登录")
    @AccessLimit(seconds = 60, maxCount = 5)
    @GetMapping("/github/render")
    public void githubRenderAuth(HttpServletResponse response) throws IOException {
        AuthRequest authRequest = getGithubAuthRequest();
        response.sendRedirect(authRequest.authorize(AuthStateUtils.createState()));
    }

    @Operation(summary = "Github登录回调")
    @AccessLimit(seconds = 60, maxCount = 5)
    @GetMapping("/github/callback")
    public void githubLogin(AuthCallback callback, HttpServletRequest request,
                            HttpServletResponse response) throws IOException {
        AuthRequest authRequest = getGithubAuthRequest();
        String parameter = oauthService.handleLogin(authRequest.login(callback),
                            request,RegisterTypeEnum.GITHUB.getRegisterType());
        response.sendRedirect(path+parameter);
/*
        http://localhost:8080/oauth/github/callback
        ?error
        =redirect_uri_mismatch&error_description
        =The+redirect_uri+MUST+match+the+registered+callback+URL+for+this+application.&error_uri
        =https%3A%2F%2Fdocs.github.com%2Fapps%2Fmanaging-oauth-apps%2Ftroubleshooting-authorization-request-errors%2F%23redirect-uri-mismatch
        &state
        =fa6636fa01dcdeb8866d6c6846dc1148

        http://127.0.0.1:8080/oauth/github/callback
        ?code=f1fa14001ad0240549a4&state=ab33168c45d4ef81d835bfb2622be5e0
*/

    }


    /**
     * 获取github授权请求
     * @return github授权请求
     */
    private AuthRequest getGithubAuthRequest() {
        return new AuthGithubRequest(AuthConfig.builder()
                .clientId(githubBody.getClientId())
                .clientSecret(githubBody.getClientSecret())
                .redirectUri(githubBody.getRedirectUri())
                .build());
    }


}
