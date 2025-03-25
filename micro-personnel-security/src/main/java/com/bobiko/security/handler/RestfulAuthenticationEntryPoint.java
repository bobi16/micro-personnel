package com.bobiko.security.handler;

import cn.hutool.json.JSONUtil;
import com.bobiko.core.constants.ResultStatus;
import com.bobiko.core.domain.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * @Author: bobiko
 * @CreateTime: 2025-03-26
 * @Description: 未登录处理器
 */
public class RestfulAuthenticationEntryPoint implements AuthenticationEntryPoint
{

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(JSONUtil.parseObj(Result.error(ResultStatus.FORBIDDEN)));
        response.getWriter().flush();
    }
}
