package com.fish.birdProducted.handler;

import com.fish.birdProducted.domain.entity.LoginUser;
import com.fish.birdProducted.domain.response.ResponseResult;
import com.fish.birdProducted.domain.vo.AuthorizeVO;
import com.fish.birdProducted.service.LoginLogService;
import com.fish.birdProducted.utils.JwtUtils;
import com.fish.birdProducted.utils.RedisCache;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import com.fish.birdProducted.constants.Const;
import com.fish.birdProducted.constants.RespConst;
import com.fish.birdProducted.enums.RespEnum;
import com.fish.birdProducted.utils.StringUtils;
import com.fish.birdProducted.utils.WebUtil;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @author fish
 * <p>
 * @创建时间 2024/2/11 16:03
 * @Content security处理器
 */
@Component
public class SecurityHandler {

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private RedisCache redisCache;

    @Resource
    private LoginLogService loginLogService;

    public static final String USER_NAME = "username";

    /**
     * 登录成功处理
     *
     * @param request        请求
     * @param response       响应
     * @param authentication 认证信息
     */
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        handlerOnAuthenticationSuccess(request,response,(LoginUser)authentication.getPrincipal());
    }

    public void handlerOnAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            LoginUser user
    ) {
        // 检查请求头中的类型是否合法
        String typeHeader = request.getHeader(Const.TYPE_HEADER);
        if ((!StringUtils.matches(typeHeader, List.of(Const.BACKEND_REQUEST, Const.FRONTEND_REQUEST)) && user.getUser().getRegisterType() == 1)) {
            throw new BadCredentialsException("非法请求");
        }
        Long id = user.getUser().getId();
        String name = user.getUser().getUsername();
        // UUID做jwt的id
        String uuid = UUID.randomUUID().toString();
        // 生成jwt
        String token = jwtUtils.createJwt(uuid, user, id, name);

        // 转换VO
        AuthorizeVO authorizeVO = user.getUser().asViewObject(AuthorizeVO.class, v -> {
            v.setToken(token);
            v.setExpire(jwtUtils.expireTime());
        });
        // TODO 更新登录状态
//        userService.userLoginStatus(user.getUser().getId());
        // 记录登录日志
        loginLogService.loginLog(request, request.getParameter(USER_NAME), 0, RespConst.SUCCESS_LOGIN_MSG);
        // 返回给客户端
        WebUtil.renderString(response, ResponseResult.success(authorizeVO, RespConst.SUCCESS_LOGIN_MSG).asJsonString());
    }


    /**
     * 登录失败处理
     */
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {
        loginLogService.loginLog(request, request.getParameter(USER_NAME), 1, exception.getMessage());
        WebUtil.renderString(response, ResponseResult.failure(RespEnum.USERNAME_OR_PASSWORD_ERROR.getCode(), exception.getMessage()).asJsonString());
    }

    /**
     * 退出登录处理
     */
    public void onLogoutSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        boolean invalidateJwt = jwtUtils.invalidateJwt(request.getHeader("Authorization"));
        if (invalidateJwt) {
            WebUtil.renderString(response, ResponseResult.success().asJsonString());
            return;
        }
        WebUtil.renderString(response, ResponseResult.failure(RespEnum.NOT_LOGIN.getCode(), RespEnum.NOT_LOGIN.getMsg()).asJsonString());
    }

    /**
     * 没有登录处理
     */
    public void onUnAuthenticated(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {
        WebUtil.renderString(response, ResponseResult.failure(RespEnum.NOT_LOGIN.getCode(), RespEnum.NOT_LOGIN.getMsg()).asJsonString());
    }

    /**
     * 没有权限处理
     */
    public void onAccessDeny(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException exception
    ) {
        WebUtil.renderString(response, ResponseResult.failure(RespEnum.NO_PERMISSION.getCode(), RespEnum.NO_PERMISSION.getMsg()).asJsonString());
    }
}
