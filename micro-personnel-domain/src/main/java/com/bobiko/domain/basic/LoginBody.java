package com.bobiko.domain.basic;

import lombok.Data;

import java.io.Serializable;

/**
 * @author bobiko
 * @date 2025-4-7 14:18
 * @description 登录请求参数
 */
@Data
public class LoginBody implements Serializable
{
    /**
     * 账号
     */
    private String username;
    /**、
     * 密码
     */
    private String password;
    /**
     * 验证码
     */
    private String code;
    /**
     * 验证码唯一key
     */
    private String cid;
    /**
     * 是否记住登录状态
     */
    private boolean rememberme;
}
