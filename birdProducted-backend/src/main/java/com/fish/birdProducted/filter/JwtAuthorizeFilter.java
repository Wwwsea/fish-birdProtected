package com.fish.birdProducted.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fish.birdProducted.domain.entity.LoginUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import com.fish.birdProducted.utils.JwtUtils;

import java.io.IOException;

/**
 * @author fish
 * <p>
 * 创建时间：2024/2/11 20:32
 * @content jwt认证
 */
@Component
public class JwtAuthorizeFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 提取 Header
        String authorization = request.getHeader("Authorization");
        System.out.println("1111111完成jwt认证");
        // 解析jwt
        DecodedJWT jwt = jwtUtils.resolveJwt(authorization);

         if (!ObjectUtils.isEmpty(jwt)) {
            // 获取UserDetails
            LoginUser user = (LoginUser) jwtUtils.toUser(jwt);
            // 创建认证对象
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            // 保存认证详细信息
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // 验证通过，设置上下文中
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        // 继续过滤器链
        filterChain.doFilter(request, response);
    }
}
