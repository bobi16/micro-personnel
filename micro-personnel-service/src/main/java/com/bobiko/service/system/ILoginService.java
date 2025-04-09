package com.bobiko.service.system;

import com.bobiko.common.core.domain.Result;
import com.bobiko.domain.basic.LoginBody;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Map;

/**
 * @author bobiko
 * @date 2025-4-7 14:15
 * @description 登录业务层接口
 */
public interface ILoginService
{
    /**
     * 登录实现
     * @param request
     * @param loginBody
     * @return
     */
    Result<String> doLogin(HttpServletRequest request, LoginBody loginBody);

    /**
     * 退出实现
     * @return
     */
    Result<String> doLogout();

    /**
     * 获取验证码实现
     * @return
     */
    Result<Map<String, Object>> getCaptcha() throws IOException;
}
