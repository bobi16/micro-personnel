package com.bobiko.security.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @Author: bobiko
 * @CreateTime: 2025-03-26
 * @Description: 已认证用户实体类
 */
public class LoginUser implements UserDetails
{
    /**
     * 用户实体类
     */
    private SysUser sysUser;

    /**
     * 用户权限信息
     */
    private List<GrantedAuthority> authorities;
    /**
     * 是否管理员
     */
    private boolean admin;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return sysUser.getPassword();
    }

    @Override
    public String getUsername() {
        return sysUser.getUserName();
    }

    public boolean isAdmin() {
        return sysUser.getUserType() == 0;
    }

    public SysUser getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }

    public void setAuthorities(List<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    /**
     * 获取用户ID
     * @return
     */
    public Long getUserId(){
        return sysUser.getUserId();
    }

    /**
     * 获取部门ID
     * @return
     */
    public Long getDeptId(){
        return sysUser.getDeptId();
    }
}
