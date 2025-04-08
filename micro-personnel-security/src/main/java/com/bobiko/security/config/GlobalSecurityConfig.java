package com.bobiko.security.config;

import com.bobiko.common.core.constants.RedisConstants;
import com.bobiko.common.redis.service.RedisService;
import com.bobiko.domain.basic.LoginUser;
import com.bobiko.domain.system.SysMenu;
import com.bobiko.security.filter.TokenAuthenticationFilter;
import com.bobiko.security.handler.RestfulAccessDeniedHandler;
import com.bobiko.security.handler.RestfulAuthenticationEntryPoint;
import com.bobiko.security.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;
import java.util.List;

/**
 * @Author: bobiko
 * @CreateTime: 2025-03-25
 * @Description: 全局security配置类
 */
@Configuration
@EnableWebSecurity
public class GlobalSecurityConfig
{

    private final RedisService redisService;

    public GlobalSecurityConfig(RedisService redisService) {
        this.redisService = redisService;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //禁用跨站请求伪造
        http.csrf(AbstractHttpConfigurer::disable);
        //禁用session
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        //配置白名单及需要认证的请求地址
        http.authorizeHttpRequests(
                authorize  -> {
                    authorize
                            //白名单进行放行
                            .requestMatchers(ignoreUrls().getUrls().toArray(new String[0])).permitAll()
                            //options进行放行
                            .requestMatchers(HttpMethod.OPTIONS).permitAll()
                            //其余请求全部需要认证
                            .anyRequest().access((authentication, object) -> {
                                String requestURI = object.getRequest().getRequestURI();
                                //获取登录对象的用户权限
                                LoginUser loginUser = SecurityUtils.getLoginUser();
                                if (null != loginUser){
                                    //超级管理所有接口均可访问
                                    if (loginUser.isAdmin()){
                                        return new AuthorizationDecision(true);
                                    }
                                    //其余用户先判断请求是否有配置权限，没有配置权限不允许访问
                                    boolean configured = isPermissionConfigured(requestURI);
                                    if (!configured){
                                        return new AuthorizationDecision(false);
                                    }
                                    //有配置权限的根据选项访问
                                    Collection<? extends GrantedAuthority> authorities = loginUser.getAuthorities();
                                    //获取当前用户权限
                                    Collection<? extends GrantedAuthority> currentUserAuthorities = authentication.get().getAuthorities();
                                    for (GrantedAuthority authority : authorities){
                                        for (GrantedAuthority current : currentUserAuthorities){
                                            if (authority.getAuthority().equalsIgnoreCase(current.getAuthority())){
                                                return new AuthorizationDecision(true);
                                            }
                                        }
                                    }
                                }
                                return new AuthorizationDecision(false);
                            });
                }
        );

        //添加JWT过滤器
        http.addFilterBefore(new TokenAuthenticationFilter(redisService), UsernamePasswordAuthenticationFilter.class);

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

    /**
     * 判断当前请求是否配置了权限
     * @param request
     * @return
     */
    private boolean isPermissionConfigured(String requestUrl) {
        List<SysMenu> sysMenus = (List<SysMenu>) redisService.get(RedisConstants.SYS_MENU_CACHE_KEY);
        for (SysMenu sysMenu : sysMenus) {
            if (sysMenu.getPath().equals(requestUrl)) {
                return true;
            }
        }
        return false;
    }
}
