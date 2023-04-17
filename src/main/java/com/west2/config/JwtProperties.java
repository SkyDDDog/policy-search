package com.west2.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * jwt配置读取类
 * @author 天狗
 */
//@Component
//@ConfigurationProperties(prefix = "jwt")
@Data
@Configuration
public class JwtProperties {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.token-head}")
    private String tokenHead;
    @Value("${jwt.header-name}")
    private String headerName;
    @Value("${jwt.access-expiration}")
    private Integer accessExpiration;
    @Value("${jwt.roles-expiration}")
    private Integer rolesExpiration;
    @Value("${jwt.refresh-expiration}")
    private Integer refreshExpiration;



}
