package com.bobiko.service.system.impl;

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
import com.bobiko.security.utils.SecurityUtils;
import com.bobiko.service.system.ILoginService;
import com.bobiko.service.system.ISysUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: bobiko
 * @CreateTime: 2025-04-09
 * @Description: 登录业务层实现
 */
@Service
public class LoginServiceImpl implements ILoginService
{
    /**
     * AuthenticationManager对象
     */
    private final AuthenticationManager authenticationManager;
    /**
     * redis服务
     */
    private final RedisService redisService;
    /**
     * 用户服务
     */
    private final ISysUserService userService;

    public LoginServiceImpl(AuthenticationManager authenticationManager, RedisService redisService, ISysUserService userService) {
        this.authenticationManager = authenticationManager;
        this.redisService = redisService;
        this.userService = userService;
    }

    /**
     * 登录实现
     * @param request
     * @param loginBody
     * @return
     */
    @Override
    public Result<String> doLogin(HttpServletRequest request, LoginBody loginBody) {
        //校验验证码是否输入
        if (StrUtil.isEmpty(loginBody.getCode())){
            return Result.error("验证码不能为空!");
        }
        //校验账号是否输入
        if (StrUtil.isEmpty(loginBody.getUsername())){
            return Result.error("账号不能为空!");
        }
        //校验密码是否输入
        if (StrUtil.isEmpty(loginBody.getPassword())){
            return Result.error("密码不能为空!");
        }
        //获取验证码并校验是否正确
        String cid = loginBody.getCid();
        if (StrUtil.isEmpty(cid)){
            return Result.error(ResultStatus.FAILED);
        }
        //获取redis中缓存的验证码
        String code = (String) redisService.get(RedisConstants.LOGIN_CAPTCHA_CODE_KEY + cid);
        if (StrUtil.isEmpty(code)){
            return Result.error("验证码不存在或已过期,请重新获取验证码!");
        }
        //判断验证码是否正确
        if (!code.equalsIgnoreCase(loginBody.getCode())){
            return Result.error("验证码错误,请重新输入!");
        }
        //进行认证操作
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginBody.getUsername(), loginBody.getPassword());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if (null == authentication){
            return Result.error(ResultStatus.AUTHORIZE_FAIL);
        }
        //生成token信息
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String username = loginUser.getUsername();
        String token = TokenUtils.createToken(username);
        //将token保存到redis中
        redisService.set(RedisConstants.LOGIN_TOKEN_KEY + username, token, TokenConstants.TOKEN_EXPIRE);
        //将用户信息保存到redis中
        redisService.set(RedisConstants.LOGIN_USER_KEY + username, loginUser, TokenConstants.TOKEN_EXPIRE);
        //更新用户登录信息
        SysUser sysUser = loginUser.getSysUser();
        sysUser.setLoginIp(IpUtils.getIpAddr(request));
        userService.updateById(sysUser);
        //返回token信息
        return Result.success(TokenConstants.TOKEN_PREFIX + token);
    }

    /**
     * 退出登录实现
     * @return
     */
    @Override
    public Result<String> doLogout() {
        String username = SecurityUtils.getUsername();
        String tokenKey = RedisConstants.LOGIN_TOKEN_KEY + username;
        String userKey = RedisConstants.LOGIN_USER_KEY + username;
        redisService.del(tokenKey, userKey);
        SecurityUtils.logout();
        return Result.success("退出登录成功!");
    }

    /**
     * 获取验证码实现
     * @return
     * @throws IOException
     */
    @Override
    public Result<Map<String, Object>> getCaptcha() throws IOException {
        Map<String, Object> result = new HashMap<>();
        //生成验证码key
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //生成验证码
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(CaptchaConstants.CAPTCHA_WIDTH,
                CaptchaConstants.CAPTCHA_HEIGHT, CaptchaConstants.CAPTCHA_LENGTH, CaptchaConstants.CAPTCHA_CIRCLE_NUM);
        //获取图形验证码
        BufferedImage image = captcha.getImage();
        //将验证码转为base64
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", stream);
        byte[] bytes = Base64.getEncoder().encode(stream.toByteArray());
        String base64 = new String(bytes);
        String codeImage = "data:image/png;base64," + base64;
        result.put("code", codeImage);
        result.put("cid", uuid);
        //将验证码保存到redis中
        redisService.set(RedisConstants.LOGIN_CAPTCHA_CODE_KEY + uuid, captcha.getCode(), CaptchaConstants.LOGIN_CAPTCHA_CODE_TTL);
        return Result.success(result);
    }
}
