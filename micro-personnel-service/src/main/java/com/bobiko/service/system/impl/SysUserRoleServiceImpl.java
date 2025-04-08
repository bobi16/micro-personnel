package com.bobiko.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bobiko.dao.system.mapper.SysUserRoleMapper;
import com.bobiko.domain.system.SysUserRole;
import com.bobiko.service.system.ISysUserRoleService;
import org.springframework.stereotype.Service;

/**
* @author bobiko
* @description 针对表【sys_user_role(用户和角色关联表)】的数据库操作Service实现
* @createDate 2025-04-08 21:37:43
*/
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole>
    implements ISysUserRoleService {

}




