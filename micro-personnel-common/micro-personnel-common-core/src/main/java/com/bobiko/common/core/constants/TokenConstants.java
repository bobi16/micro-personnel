package com.bobiko.common.core.constants;

import java.util.UUID;

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
    long TOKEN_EXPIRE = 7200 * 1000;
    /**
     * token加密秘钥
     */
    String SECRET_KEY = UUID.randomUUID().toString().replaceAll("-", "");
}
