package com.bobiko.security.handler;

import cn.hutool.json.JSONUtil;
import com.bobiko.core.constants.ResultStatus;
import com.bobiko.core.domain.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

/**
 * @Author: bobiko
 * @CreateTime: 2025-03-25
 * @Description: 无权限处理器
 */
public class RestfulAccessDeniedHandler implements AccessDeniedHandler
{

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(JSONUtil.parseObj(Result.error(ResultStatus.UNAUTHORIZED)));
        response.getWriter().flush();
    }
}
