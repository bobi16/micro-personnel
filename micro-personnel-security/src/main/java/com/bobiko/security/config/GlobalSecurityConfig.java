package com.bobiko.security.config;

import com.bobiko.security.handler.RestfulAccessDeniedHandler;
import com.bobiko.security.handler.RestfulAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @Author: bobiko
 * @CreateTime: 2025-03-25
 * @Description: 全局security配置类
 */
@Configuration
@EnableWebSecurity
public class GlobalSecurityConfig
{
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //禁用跨站请求伪造
        http.csrf(AbstractHttpConfigurer::disable);
        //禁用session
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        //配置白名单及需要认证的请求地址
        http.authorizeHttpRequests(
                authorize  -> authorize
                        //白名单进行放行
                        .requestMatchers(ignoreUrls().getUrls().toArray(new String[0])).permitAll()
                        //options进行放行
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
                        //其余请求全部需要认证
                        .anyRequest().authenticated()
        );

        //定义异常处理
        http.exceptionHandling(
                ex -> ex.accessDeniedHandler(restfulAccessDeniedHandler())
                        .authenticationEntryPoint(restfulAuthenticationEntryPoint())
        );

        return http.build();
    }

    /**
     * 白名单
     * @return
     */
    @Bean
    public IgnoreUrlsConfig ignoreUrls()
    {
        return new IgnoreUrlsConfig();
    }

    /**
     * 自定义无权限处理器
     * @return
     */
    @Bean
    public RestfulAccessDeniedHandler restfulAccessDeniedHandler()
    {
        return new RestfulAccessDeniedHandler();
    }

    /**
     * 自定义未授权处理器
     * @return
     */
    @Bean
    public RestfulAuthenticationEntryPoint restfulAuthenticationEntryPoint()
    {
        return new RestfulAuthenticationEntryPoint();
    }

    /**
     * 密码加密器
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return  new BCryptPasswordEncoder();
    }
}
