package com.bobiko.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bobiko.domain.system.SysUser;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
* @author bobik
* @description 针对表【sys_user(用户信息表)】的数据库操作Service
* @createDate 2025-03-27 01:03:30
*/
public interface ISysUserService extends IService<SysUser>, UserDetailsService {

}
