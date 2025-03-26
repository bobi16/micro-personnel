package com.bobiko.security.filter;

import cn.hutool.core.util.StrUtil;
import com.bobiko.common.core.constants.TokenConstants;
import com.bobiko.common.core.utils.TokenUtils;
import com.bobiko.common.redis.service.RedisService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @Author: bobiko
 * @CreateTime: 2025-03-26
 * @Description: token认证过滤器
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter
{
    private final RedisService redisService;

    public TokenAuthenticationFilter(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(TokenConstants.TOKEN_HEADER);
        //如果获取不到token直接放行
        if (StrUtil.isNotEmpty(token)){
            //验证token是否过期
            boolean expired = TokenUtils.isTokenExpired(token);
            if (!expired){
                //从token中获取用户名
                String username = TokenUtils.getUsernameFromToken(token);
            }
        }
        filterChain.doFilter(request, response);
    }
}
