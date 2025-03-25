package com.bobiko.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: bobiko
 * @CreateTime: 2025-03-25
 * @Description: 请求白名单
 */
@ConfigurationProperties(prefix = "security.ignore")
public class IgnoreUrlsConfig implements Serializable
{
    private List<String> urls;

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}
