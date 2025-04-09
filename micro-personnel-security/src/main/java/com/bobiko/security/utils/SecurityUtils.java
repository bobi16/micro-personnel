package com.bobiko.security.utils;

import cn.hutool.core.util.StrUtil;
import com.bobiko.domain.basic.LoginUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author bobiko
 * @date 2025-4-8 20:30
 * @description 用户信息获取工具类
 */
public class SecurityUtils
{
    /**
     * 获取当前登录用户对象
     * @return
     */
    public static LoginUser getLoginUser()
    {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        //白名单用户
        if (authentication instanceof AnonymousAuthenticationToken){
            return null;
        }
        boolean authenticated = authentication.isAuthenticated();
        if (authenticated){
            return (LoginUser) authentication.getPrincipal();
        }else {
            return null;
        }
    }

    /**
     * 获取当前登录用户名
     * @return
     */
    public static String getUsername() {
        LoginUser loginUser = getLoginUser();
        if (null != loginUser) return loginUser.getUsername();
        return null;
    }

    /**
     * 获取当前登录用户类型
     * @return
     */
    public static Integer getUserType()
    {
        LoginUser loginUser = getLoginUser();
        if (null != loginUser) return loginUser.getSysUser().getUserType();
        return null;
    }

    /**
     * 获取当前登录用户类型
     * @return
     */
    public static Long getUserId()
    {
        LoginUser loginUser = getLoginUser();
        if (null != loginUser) return loginUser.getUserId();
        return null;
    }

    /**
     * 清理上下文对象
     */
    public static void logout() {
        String username = getUsername();
        if (StrUtil.isNotEmpty(username)){
            SecurityContextHolder.clearContext();
        }
    }
}
