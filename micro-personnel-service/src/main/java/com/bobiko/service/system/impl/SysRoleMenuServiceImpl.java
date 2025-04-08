package com.bobiko.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bobiko.dao.system.mapper.SysRoleMenuMapper;
import com.bobiko.domain.system.SysRoleMenu;
import com.bobiko.service.system.ISysRoleMenuService;
import org.springframework.stereotype.Service;

/**
* @author bobiko
* @description 针对表【sys_role_menu(角色和菜单关联表)】的数据库操作Service实现
* @createDate 2025-04-08 21:41:10
*/
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu>
    implements ISysRoleMenuService {

}




