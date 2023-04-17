package com.west2.config.security;

import com.west2.config.JwtProperties;
import com.west2.util.JwtTokenUtil;
import com.west2.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class MyAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private MyUserDetailsService myUserDetailsService;
    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //根据输入的用户密码，读取数据库中信息
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        MyUserDetails user = (MyUserDetails) myUserDetailsService.loadUserByUsername(username);
        log.info(user.toString());
        //判断是否有效用户
        if (!user.isEnabled()) {
            throw new DisabledException("USER DISABLE");
        } else if (!user.isAccountNonLocked()) {
            throw new LockedException("USER LOCKED");
        } else if (!user.isAccountNonExpired()) {
            throw new AccountExpiredException("USER EXPIRED");
        } else if (!user.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("USER LOGOUT");
        }

        //验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("PASSWORD INVALID!");
        }


        log.info(String.format("用户%s登录成功", username));
        // 生成新token
        Map<String,String> tokens = jwtTokenUtil.generateToken(user);
        String accesstoken = tokens.get(jwtTokenUtil.getAccessTokenKey());
        String refreshtoken = tokens.get(jwtTokenUtil.getRefreshTokenKey());
        String rolestoken = tokens.get(jwtTokenUtil.getRoleTokenKey());
        // 保存到 redis
        RedisUtil.StringOps.setEx(jwtTokenUtil.getAccessTokenKey(username), accesstoken, jwtProperties.getAccessExpiration(), TimeUnit.SECONDS);
        RedisUtil.StringOps.setEx(jwtTokenUtil.getRefreshTokenKey(username), refreshtoken, jwtProperties.getRefreshExpiration(), TimeUnit.SECONDS);
        RedisUtil.StringOps.setEx(jwtTokenUtil.getRoleTokenKey(username), rolestoken, jwtProperties.getRolesExpiration(), TimeUnit.SECONDS);
        // 绑定到当前用户
        user.setAccessToken(accesstoken);
        user.setRefreshToken(refreshtoken);
        return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
