package com.bobiko.security.filter;

import cn.hutool.core.util.StrUtil;
import com.bobiko.common.core.constants.RedisConstants;
import com.bobiko.common.core.constants.TokenConstants;
import com.bobiko.common.core.utils.TokenUtils;
import com.bobiko.common.redis.service.RedisService;
import com.bobiko.domain.basic.LoginUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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
        if (StrUtil.isNotEmpty(token) && token.startsWith(TokenConstants.TOKEN_PREFIX)) {
            token = token.replace(TokenConstants.TOKEN_PREFIX, "");
            //验证token是否过期
            boolean expired = TokenUtils.isTokenExpired(token);
            if (!expired) {
                //从token中获取用户名
                String username = TokenUtils.getUsernameFromToken(token);
                String userCacheKey = RedisConstants.LOGIN_USER_KEY + username;
                String tokenCacheKey = RedisConstants.LOGIN_TOKEN_KEY + username;
                //可以获取到用户信息，将用户信息存入securityContext中
                if (StrUtil.isNotEmpty(username)) {
                    LoginUser loginUser = (LoginUser) redisService.get(userCacheKey);
                    if (null != loginUser) {
                        //判断用户上次操作的时间是否超过30分钟
                        if (System.currentTimeMillis() > loginUser.getLastAccessTime() + TokenConstants.MAX_NON_OPER_TIME) {
                            //清除redis中的认证信息
                            redisService.del(userCacheKey, tokenCacheKey);
                            //清理Security的上下文信息
                            SecurityContextHolder.clearContext();
                        }else{
                            //获取redis中token的过期时间
                            long expire = redisService.getExpire(userCacheKey);
                            //如果在token的过期时间指定范围之前，则刷新token
                            if (System.currentTimeMillis() < TokenConstants.REFRESH_TOKEN_EXPIRE + expire){
                                String refreshToken = TokenUtils.createToken(loginUser.getUsername());
                                redisService.set(RedisConstants.LOGIN_TOKEN_KEY + username, refreshToken, TokenConstants.REFRESH_TOKEN_EXPIRE);
                                response.setHeader(TokenConstants.REFRESH_TOKEN_HEADER, refreshToken);
                            }
                            loginUser.setLastAccessTime(System.currentTimeMillis());
                            //将用户信息存入securityContext中
                            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                                    new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
                            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                        }
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}