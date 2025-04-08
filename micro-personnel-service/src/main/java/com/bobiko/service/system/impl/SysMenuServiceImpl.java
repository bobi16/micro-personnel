package com.bobiko.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.bobiko.common.core.constants.RedisConstants;
import com.bobiko.common.redis.service.RedisService;
import com.bobiko.dao.system.mapper.SysMenuMapper;
import com.bobiko.domain.constant.SystemConstants;
import com.bobiko.domain.system.SysMenu;
import com.bobiko.service.system.ISysMenuService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author bobiko
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Service实现
* @createDate 2025-04-08 21:41:10
*/
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu>
    implements ISysMenuService {

    private final RedisService redisService;

    public SysMenuServiceImpl(RedisService redisService) {
        this.redisService = redisService;
    }

    @PostConstruct
    public void sysMenuCache(){
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysMenu::getMenuType, SystemConstants.SYS_MENU_TYPE_INTERFACE);
        List<SysMenu> sysMenus = this.baseMapper.selectList(queryWrapper);
        redisService.set(RedisConstants.SYS_MENU_CACHE_KEY, sysMenus);
    }
}




