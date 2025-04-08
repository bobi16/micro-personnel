package com.bobiko.common.core.constants;

public interface TokenConstants
{
    /**
     * token请求头标识
     */
    String TOKEN_HEADER = "X-Token";
    /**
     * 刷新token请求头标识
     */
    String REFRESH_TOKEN_HEADER = "R-Token";
    /**
     * token过期时长，默认2小时
     */
    long TOKEN_EXPIRE = 7200;
    /**
     * 刷新token过期时长，默认30分钟
     */
    long REFRESH_TOKEN_EXPIRE = 1800000;
    /**
     * 最大不操作时长，默认30分钟
     */
    long MAX_NON_OPER_TIME = 1800000;
    /**
     * token加密秘钥
     */
    String SECRET_KEY = "b03dee44147b11f0adfb00163e049a64";
    /**
     * token前缀
     */
    String TOKEN_PREFIX = "Bearer ";
}
