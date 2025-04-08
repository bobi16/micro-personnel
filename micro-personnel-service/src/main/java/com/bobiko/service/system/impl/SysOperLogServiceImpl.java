package com.bobiko.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.bobiko.dao.system.mapper.SysOperLogMapper;
import com.bobiko.domain.system.SysOperLog;
import com.bobiko.service.system.ISysOperLogService;
import org.springframework.stereotype.Service;

/**
* @author bobiko
* @description 针对表【sys_oper_log(操作日志记录)】的数据库操作Service实现
* @createDate 2025-04-08 20:18:58
*/
@Service
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLog>
    implements ISysOperLogService {

}




