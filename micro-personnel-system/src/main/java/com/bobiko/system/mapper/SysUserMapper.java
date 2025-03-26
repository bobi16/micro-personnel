package com.bobiko.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bobiko.security.domain.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
* @author bobik
* @description 针对表【sys_user(用户信息表)】的数据库操作Mapper
* @createDate 2025-03-27 01:03:30
* @Entity com.bobiko.system.domain.SysUser
*/
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

}




