package com.bobiko.common.core.utils;

import com.bobiko.common.core.constants.TokenConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: bobiko
 * @CreateTime: 2025-03-26
 * @Description: token相关工具类
 */
public class TokenUtils
{
    /**
     * 秘钥
     */
    private static final Key key = Keys.hmacShaKeyFor(TokenConstants.SECRET_KEY.getBytes());

    /**
     * 根据用户名创建token
     * @param username
     * @return
     */
    public static String createToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        return createToken(claims, username);
    }

    /**
     * 自定义实现创建token
     * @param claims
     * @param subject
     * @return
     */
    public static String createToken(Map<String, Object> claims, String subject) {
        Date expirationDate = new Date(new Date().getTime() + TokenConstants.TOKEN_EXPIRE * 1000);
        return Jwts.builder().claims(claims).subject(subject).expiration(expirationDate).signWith(key).compact();
    }

    /**
     * 从token获取所有载荷
     * @param token
     * @return
     */
    private static Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getPayload();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * 从token中获取用户名
     * @param token
     * @return
     */
    public static String getUsernameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;

    }

    /**
     * 判断token是否过期
     * @param token
     * @return
     */
    public static boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 获取token过期时间
     * @param token
     * @return
     */
    public static Date tokenExpiredAt(String token){
        return getClaimsFromToken(token).getExpiration();
    }

    /**
     * 获取刷新token
     * @param token
     * @return
     */
    public static String refreshToken(String token) {
        String refreshedToken;
        try {
            Claims claims = getClaimsFromToken(token);
            String username = getUsernameFromToken(token);
            refreshedToken = createToken(claims, username);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }
}
