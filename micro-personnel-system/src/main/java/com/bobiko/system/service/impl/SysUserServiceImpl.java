package com.bobiko.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bobiko.security.domain.LoginUser;
import com.bobiko.security.domain.SysUser;
import com.bobiko.system.service.ISysUserService;
import com.bobiko.system.mapper.SysUserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
* @author bobik
* @description 针对表【sys_user(用户信息表)】的数据库操作Service实现
* @createDate 2025-03-27 01:03:30
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements ISysUserService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(StrUtil.isEmpty(username)){
            throw new UsernameNotFoundException("账号或密码错误!");
        }
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysUser::getUserName, username);
        SysUser sysUser = this.baseMapper.selectOne(queryWrapper);
        if(null == sysUser){
            throw new UsernameNotFoundException("账号或密码错误!");
        }
        LoginUser loginUser = new LoginUser();
        loginUser.setSysUser(sysUser);
        return loginUser;
    }
}




