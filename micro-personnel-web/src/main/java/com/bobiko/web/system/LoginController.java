package com.bobiko.web.system;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.core.util.StrUtil;
import com.bobiko.common.core.constants.CaptchaConstants;
import com.bobiko.common.core.constants.RedisConstants;
import com.bobiko.common.core.constants.ResultStatus;
import com.bobiko.common.core.constants.TokenConstants;
import com.bobiko.common.core.domain.Result;
import com.bobiko.common.core.utils.IpUtils;
import com.bobiko.common.core.utils.TokenUtils;
import com.bobiko.common.redis.service.RedisService;
import com.bobiko.domain.basic.LoginBody;
import com.bobiko.domain.basic.LoginUser;
import com.bobiko.domain.system.SysUser;
import com.bobiko.log.annotation.Log;
import com.bobiko.security.utils.SecurityUtils;
import com.bobiko.service.system.ILoginService;
import com.bobiko.service.system.ISysUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author bobiko
 * @date 2025-4-7 14:15
 * @description 登录接口
 */
@RequestMapping("/auth")
@RestController
public class LoginController
{
    private final ILoginService loginService;

    public LoginController(ILoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * 执行登录
     * @param loginBody
     * @return
     */

    @PostMapping("/login")
    @Log(title = "登录")
    public Result<String> doLogin(HttpServletRequest request, @RequestBody LoginBody loginBody)
    {
        return loginService.doLogin(request, loginBody);
    }


    /**
     * 退出登录
     * @return
     */
    @PostMapping("/logout")
    public Result<String> doLogout(){
        return loginService.doLogout();
    }

    /**
     * 获取登录验证码
     * @return
     * @throws IOException
     */
    @GetMapping("/code")
    @Log(title = "获取验证码")
    public Result<Map<String, Object>> getCaptcha() throws IOException {
        return loginService.getCaptcha();
    }
}