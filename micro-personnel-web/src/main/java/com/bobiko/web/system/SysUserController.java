package com.bobiko.web.system;

import com.bobiko.log.annotation.Log;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: bobiko
 * @CreateTime: 2025-03-27
 * @Description: 用户管理相关接口类
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController
{
    @GetMapping
    @Log(title = "获取用户信息")
    public String getUser(){
        return "AA";
    }
}
