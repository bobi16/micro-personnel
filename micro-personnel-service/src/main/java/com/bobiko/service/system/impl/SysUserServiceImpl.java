package com.bobiko.service.system.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bobiko.dao.system.mapper.SysMenuMapper;
import com.bobiko.dao.system.mapper.SysRoleMenuMapper;
import com.bobiko.dao.system.mapper.SysUserMapper;
import com.bobiko.dao.system.mapper.SysUserRoleMapper;
import com.bobiko.domain.basic.LoginUser;
import com.bobiko.domain.system.SysMenu;
import com.bobiko.domain.system.SysRoleMenu;
import com.bobiko.domain.system.SysUser;
import com.bobiko.domain.constant.SystemConstants;
import com.bobiko.domain.system.SysUserRole;
import com.bobiko.service.system.ISysUserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

/**
* @author bobik
* @description 针对表【sys_user(用户信息表)】的数据库操作Service实现
* @createDate 2025-03-27 01:03:30
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements ISysUserService {

    private final SysUserRoleMapper sysUserRoleMapper;

    private final SysRoleMenuMapper sysRoleMenuMapper;

    private final SysMenuMapper sysMenuMapper;

    public SysUserServiceImpl(SysUserRoleMapper sysUserRoleMapper, SysRoleMenuMapper sysRoleMenuMapper, SysMenuMapper sysMenuMapper) {
        this.sysUserRoleMapper = sysUserRoleMapper;
        this.sysRoleMenuMapper = sysRoleMenuMapper;
        this.sysMenuMapper = sysMenuMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysUser::getUserName, username);
        SysUser sysUser = this.baseMapper.selectOne(queryWrapper);
        //如果账号不存在抛出异常
        if(null == sysUser){
            throw new UsernameNotFoundException("账号或密码错误!");
        }
        //封装登录用户对象想想
        LoginUser loginUser = new LoginUser();
        sysUser.setLoginDate(new Date());
        loginUser.setSysUser(sysUser);
        loginUser.setLastAccessTime(System.currentTimeMillis());
        //生成权限集合
        if (Objects.equals(sysUser.getUserType(), SystemConstants.SYS_USER_TYPE_ADMIN)){
            loginUser.setAuthorities(List.of(new SimpleGrantedAuthority("*:*:*")));
        }else{
            List<GrantedAuthority> authorities = new ArrayList<>();
            QueryWrapper<SysUserRole> sysUserRoleQueryWrapper = new QueryWrapper<>();
            sysUserRoleQueryWrapper.lambda().eq(SysUserRole::getUserId, sysUser.getUserId());
            List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectList(sysUserRoleQueryWrapper);
            sysUserRoles.forEach(sysUserRole -> {
                QueryWrapper<SysRoleMenu> sysRoleMenuQueryWrapper = new QueryWrapper<>();
                sysRoleMenuQueryWrapper.lambda().eq(SysRoleMenu::getRoleId, sysUserRole.getRoleId());
                List<SysRoleMenu> sysRoleMenus = sysRoleMenuMapper.selectList(sysRoleMenuQueryWrapper);
                sysRoleMenus.forEach(sysRoleMenu -> {
                    QueryWrapper<SysMenu> sysMenuQueryWrapper = new QueryWrapper<>();
                    sysMenuQueryWrapper.lambda().eq(SysMenu::getMenuId, sysRoleMenu.getMenuId());
                    List<SysMenu> sysMenus = sysMenuMapper.selectList(sysMenuQueryWrapper);
                    sysMenus.forEach(sysMenu -> {
                        if (StrUtil.isNotEmpty(sysMenu.getPerms())){
                            authorities.add(new SimpleGrantedAuthority(sysMenu.getPerms()));
                        }
                    });
                });
            });
            loginUser.setAuthorities(authorities);
        }
        return loginUser;
    }
}




