package com.bobiko.common.core.constants;

/**
 * @author bobiko
 * @date 2025-4-7 14:45
 * @description redis相关常量类
 */
public interface RedisConstants
{
    /**
     * 登录验证码缓存key
     */
    String LOGIN_CAPTCHA_CODE_KEY = "login:code:";
    /**
     * token存储在redis中的key
     */
    String LOGIN_TOKEN_KEY = "login:token:";
    /**
     * 登录用户存储在redis中的key
     */
    String LOGIN_USER_KEY = "login:user:";
    /**
     * 菜单缓存key
     */
    String SYS_MENU_CACHE_KEY = "sys:config:menu";
    /**
     * 自动识别json对象白名单配置（仅允许解析的包名，范围越小越安全）
     */
    public static final String[] JSON_WHITELIST_STR = { "org.springframework", "com.bobiko" };
}
